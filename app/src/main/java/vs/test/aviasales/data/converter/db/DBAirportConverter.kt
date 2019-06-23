package vs.test.aviasales.data.converter.db

import vs.test.aviasales.data.getOrDie
import vs.test.aviasales.data.model.db.DBAirport
import vs.test.aviasales.domain.model.Airport

object DBAirportConverter {
    fun fromDatabase(airport: DBAirport): Airport = Airport(
            code = getOrDie(airport.code, "code"),
            cityName = getOrDie(airport.cityName, "cityName"),
            countryName = getOrDie(airport.countryName, "countryName"),
            locationFullName = getOrDie(airport.locationFullName, "locationFullName"),
            coordinates = DBCoordinatesConverter.fromDatabase(getOrDie(airport.coordinates, "coordinates"))
        )

    fun toDatabase(airport: Airport): DBAirport = DBAirport(
        code = getOrDie(airport.code, "code"),
        cityName = getOrDie(airport.cityName, "cityName"),
        countryName = getOrDie(airport.countryName, "countryName"),
        locationFullName = getOrDie(airport.locationFullName, "locationFullName"),
        coordinates = DBCoordinatesConverter.toDatabse(getOrDie(airport.coordinates, "coordinates"))
    )
}