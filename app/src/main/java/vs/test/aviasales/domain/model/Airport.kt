package vs.test.aviasales.domain.model

import vs.test.aviasales.ui.adapter.DisplayableItem
import java.io.Serializable

data class Airport(
    val code: String,
    val cityName: String,
    val countryName: String,
    val locationFullName: String,
    val coordinates: Coordinates
) : Serializable, DisplayableItem