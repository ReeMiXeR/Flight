package vs.test.aviasales.data.converter.db

import vs.test.aviasales.data.getOrDie
import vs.test.aviasales.data.model.db.DBCoordinates
import vs.test.aviasales.domain.model.Coordinates

object DBCoordinatesConverter {
    fun toDatabse(coordinates: Coordinates): DBCoordinates = DBCoordinates(
        lat = getOrDie(coordinates.lat, "lat"),
        lon = getOrDie(coordinates.lon, "lon")
    )

    fun fromDatabase(coordinates: DBCoordinates): Coordinates = Coordinates(
        lat = getOrDie(coordinates.lat, "lat"),
        lon = getOrDie(coordinates.lon, "lon")
    )
}