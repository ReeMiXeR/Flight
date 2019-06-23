package vs.test.aviasales.data.converter.network

import vs.test.aviasales.data.getOrDie
import vs.test.aviasales.data.model.network.NWCity
import vs.test.aviasales.domain.model.City

object NWCityConverter {
    fun fromNetwork(city: NWCity): City = City(
        name = getOrDie(city.name, "name"),
        fullName = getOrDie(city.fullName, "locationFullName"),
        airports = getOrDie(city.airportsCodes, "airportsCodes"),
        country = getOrDie(city.country, "country"),
        coordinates = NWCoordinatesConverter.fromNetwork(
            getOrDie(
                city.coordinates,
                "location"
            )
        )
    )
}