package vs.test.aviasales.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.City

interface SuggestRepository {
    fun searchSuggest(query: String): Single<List<City>>
    fun getHistory(): Single<List<Airport>>
    fun insertAirport(airport: Airport): Completable
}