package vs.test.aviasales.data.converter.network

import vs.test.aviasales.data.model.network.NWCoordinates
import vs.test.aviasales.data.getOrDie
import vs.test.aviasales.domain.model.Coordinates

object NWCoordinatesConverter {
    fun fromNetwork(coordinates: NWCoordinates): Coordinates = Coordinates(
        lat = getOrDie(coordinates.lat, "lat"),
        lon = getOrDie(coordinates.lon, "lon")
    )
}