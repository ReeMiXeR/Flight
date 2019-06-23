package vs.test.aviasales.domain.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Coordinates(
    val lat: Float,
    val lon: Float
): Serializable

fun Coordinates.toLatLng() = LatLng(lat.toDouble(), lon.toDouble())

infix fun Float.and(y: Float) = Coordinates(this, y)

fun Pair<Coordinates, Coordinates>.toControl1(): Coordinates {
    val (startPoint, endPoint) = this
    return Coordinates(
        // x = (𝑥1+𝑥2+𝑦2−𝑦1) / 2
        ((startPoint.lat + endPoint.lat + endPoint.lon + -startPoint.lon) / 2),
        // y = (𝑦1+𝑦2+𝑥1−𝑥2) / 2
        ((startPoint.lon + endPoint.lon + startPoint.lat - endPoint.lat) / 2)
    )
}

fun Pair<Coordinates, Coordinates>.toControl2(): Coordinates {
    val (startPoint, endPoint) = this
    return Coordinates(
        // x = (𝑥1+𝑥2+𝑦1−𝑦2) / 2
        ((startPoint.lat + endPoint.lat + startPoint.lon - endPoint.lon) / 2),
        // y = (𝑦1+𝑦2+𝑥2−𝑥1) / 2
        ((startPoint.lon + endPoint.lon + endPoint.lat - startPoint.lat) / 2)
    )
}