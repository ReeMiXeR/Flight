package vs.test.aviasales.data.model.network

import com.google.gson.annotations.SerializedName

data class NWSuggestsResponse(
    @SerializedName("cities")
    val cities: List<NWCity>? = null
)