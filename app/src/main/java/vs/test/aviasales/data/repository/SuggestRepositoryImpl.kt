package vs.test.aviasales.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import vs.test.aviasales.data.converter.network.NWCityConverter
import vs.test.aviasales.data.network.Api
import vs.test.aviasales.data.storage.PersistentStorage
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.City
import vs.test.aviasales.domain.repository.SuggestRepository

class SuggestRepositoryImpl(
    private val api: Api,
    private val storage: PersistentStorage
) : SuggestRepository {

    override fun getHistory(): Single<List<Airport>> = storage.getAirportsHistory()

    override fun insertAirport(airport: Airport): Completable = storage.insertAirportHistory(airport)

    override fun searchSuggest(query: String): Single<List<City>> {
        return api
            .searchSuggests(query)
            .map { it.cities?.map(NWCityConverter::fromNetwork) ?: emptyList() }
    }
}