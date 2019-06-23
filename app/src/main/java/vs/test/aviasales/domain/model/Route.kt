package vs.test.aviasales.domain.model

import java.io.Serializable

data class Route(
    val from: Airport,
    val to: Airport
): Serializable

fun Route.swap() = Route(to, from)