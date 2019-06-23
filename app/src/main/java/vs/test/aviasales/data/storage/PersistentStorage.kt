package vs.test.aviasales.data.storage

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.Route

interface PersistentStorage {
    fun insertAirportHistory(airport: Airport): Completable
    fun getAirportsHistory(): Single<List<Airport>>
    fun insertRouteHistory(route: Route)
    fun getRouteCodesHistory(): Single<Pair<String, String>>
    fun getRouteByCodes(codes: Pair<String, String>): Single<Route>
}