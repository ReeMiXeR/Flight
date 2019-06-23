package vs.test.aviasales.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable
import vs.test.aviasales.data.model.db.DBAirport


@Dao
interface DaoAirport : BaseDao<DBAirport> {
    @Query("SELECT * FROM airport ORDER BY airport.airport_id DESC")
    fun getAll(): Flowable<List<DBAirport>>

    @Query("DELETE FROM airport")
    fun deleteAll()

//    @Query("SELECT * FROM airport WHERE airport_id LIKE :id")
//    fun getByCode(id: Int): Flowable<DBAirport>
}