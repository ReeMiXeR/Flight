package vs.test.aviasales.domain.model

import kotlin.math.pow

data class Bezier(
    private val start: Coordinates,
    private val end: Coordinates,
    private val control1: Coordinates,
    private val control2: Coordinates
) {

    fun calculate(t: Float): Coordinates {
        checkRange(t)

        val lat = (1 - t)
            .pow(3) * start.lat + 3 * t * (1 - t)
            .pow(2) * control1.lat + 3 * t
            .pow(2) * (1 - t) * control2.lat + t
            .pow(3) * end.lat

        val lon = (1 - t)
            .pow(3) * start.lon + 3 * t * (1 - t)
            .pow(2) * control1.lon + 3 * t
            .pow(2) * (1 - t) * control2.lon + t
            .pow(3) * end.lon

        return Coordinates(lat, lon)
    }

    private fun checkRange(t: Float) {
        if (t !in 0f..1f) throw IllegalArgumentException("t should be in range [0,1]")
    }
}