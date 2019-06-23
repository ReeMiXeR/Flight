package vs.test.aviasales.domain.model

data class City(
    val name: String,
    val country: String,
    val fullName: String,
    val airports: List<String>,
    val coordinates: Coordinates
)