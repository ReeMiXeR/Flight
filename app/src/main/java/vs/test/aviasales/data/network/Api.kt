package vs.test.aviasales.data.network

import io.reactivex.Single
import vs.test.aviasales.data.model.network.NWSuggestsResponse

interface Api {
    fun searchSuggests(query: String): Single<NWSuggestsResponse>
}