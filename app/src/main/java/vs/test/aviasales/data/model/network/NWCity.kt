package vs.test.aviasales.data.model.network

import com.google.gson.annotations.SerializedName

data class NWCity(
    @SerializedName("fullname")
    val fullName: String? = null,
    @SerializedName("city")
    val name: String? = null,
    @SerializedName("iata")
    val airportsCodes: List<String>? = null,
    @SerializedName("location")
    val coordinates: NWCoordinates? = null,
    @SerializedName("country")
    val country: String? = null
)