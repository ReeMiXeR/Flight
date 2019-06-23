package vs.test.aviasales.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import vs.test.aviasales.data.db.dao.DaoAirport
import vs.test.aviasales.data.model.db.DBAirport

@Database(
    entities = [DBAirport::class],
    version = 2
)
abstract class Database : RoomDatabase() {
    abstract fun daoAirport(): DaoAirport
}