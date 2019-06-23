package vs.test.aviasales.data.model.network

import com.google.gson.annotations.SerializedName

data class NWCoordinates(
    @SerializedName("lat")
    val lat: Float?,
    @SerializedName("lon")
    val lon: Float?
)