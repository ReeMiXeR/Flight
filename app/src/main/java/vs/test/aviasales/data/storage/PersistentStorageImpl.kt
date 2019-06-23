package vs.test.aviasales.data.storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import vs.test.aviasales.BuildConfig
import vs.test.aviasales.data.converter.db.DBAirportConverter
import vs.test.aviasales.data.db.Database
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.utils.defaultRoute

class PersistentStorageImpl(private val context: Context) : PersistentStorage {

    private companion object {
        const val TAG = "PersistentStorageImpl"
        const val KEY_ROUTE_FROM = "$TAG.route_from"
        const val KEY_ROUTE_TO = "$TAG.route_to"
        const val DEFAULT_AIRPORTS_ASSET = "default_data.json"
        const val DB_NAME = "FlightBase.db"
    }

    private val db by lazy {
        Timber.tag(TAG).d("asdad")
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            DB_NAME
        )
            .addCallback(getFirstLaunchCallback())
            .fallbackToDestructiveMigration()
            .build()
    }

    private val preference by lazy { context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE) }

    override fun insertAirportHistory(airport: Airport): Completable {
        return Completable.fromAction {
            val dbItem = DBAirportConverter.toDatabase(airport)
            db.daoAirport().insert(dbItem)
        }
    }

    override fun getAirportsHistory(): Single<List<Airport>> {
        return db.daoAirport()
            .getAll()
            .firstOrError()
            .map { it.map(DBAirportConverter::fromDatabase) }
    }

    override fun insertRouteHistory(route: Route) {
        putCodesToPrefs(route.from.code, route.to.code)
    }

    override fun getRouteCodesHistory(): Single<Pair<String, String>> {
        return Single.fromCallable {
            val fromCode = preference.getString(KEY_ROUTE_FROM, null)
            val toCode = preference.getString(KEY_ROUTE_TO, null)

            if (fromCode != null && toCode != null) {
                fromCode to toCode
            } else {
                (defaultRoute.from.code to defaultRoute.to.code).also { (from, to) ->
                    putCodesToPrefs(from, to)
                }
            }
        }
    }

    override fun getRouteByCodes(codes: Pair<String, String>): Single<Route> {
        return db.daoAirport()
            .getAll()
            .firstOrError()
            .map { it.map(DBAirportConverter::fromDatabase) }
            .map { allAirports ->
                Route(
                    from = allAirports.firstOrNull { it.code.equals(codes.first, true) } ?: defaultRoute.from,
                    to = allAirports.firstOrNull { it.code.equals(codes.second, true) } ?: defaultRoute.to
                )
            }
    }

//    private fun getAirportFromDB(id: Int, defaultValue: Airport): Single<Airport> {
//        return db.daoAirport().getByCode(id)
//            .timeout(300, TimeUnit.MILLISECONDS)
//            .firstElement()
//            .map { DBAirportConverter.fromDatabase(it) }
//            .defaultIfEmpty(defaultValue)
//            .toSingle()
//    }

    private fun getFirstLaunchCallback() = object : RoomDatabase.Callback() {
        // Заполняем базу при первом запуске
        override fun onCreate(db: SupportSQLiteDatabase) {

            val string = context.assets.open(DEFAULT_AIRPORTS_ASSET)
                .bufferedReader()
                .use {
                    it.readText()
                }

            val defaultAirports = Gson().fromJson<List<Airport>>(
                string,
                object : TypeToken<List<Airport>>() {}.type
            ).let { it.map(DBAirportConverter::toDatabase) }

            db.beginTransaction()

            try {
                // Пишем массив DBPlaceTagGroup в базу
                defaultAirports.forEach {
                    val (groupTableName, groupConflict, groupValue) = it.toInsertField()
                    db.insert(groupTableName, groupConflict, groupValue)
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
    }

    private fun putCodesToPrefs(fromCode: String, toCode: String) {
        preference.edit().apply {
            putString(KEY_ROUTE_FROM, fromCode)
            putString(KEY_ROUTE_TO, toCode)
            apply()
        }
    }
}