package vs.test.aviasales.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import vs.test.aviasales.data.model.network.NWSuggestsResponse

interface ApiService {
    @GET("autocomplete")
    fun searchSuggests(
        @Query("term") query: String,
        @Query("lang") lang: String = "ru"
    ): Single<NWSuggestsResponse>
}