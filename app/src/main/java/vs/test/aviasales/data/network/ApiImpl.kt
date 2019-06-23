package vs.test.aviasales.data.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vs.test.aviasales.data.model.network.NWSuggestsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



class ApiImpl : Api {

     private val service: ApiService

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://yasen.hotellook.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        service = retrofit.create(ApiService::class.java)
    }

    override fun searchSuggests(query: String): Single<NWSuggestsResponse> {
        return service.searchSuggests(query)
    }
}