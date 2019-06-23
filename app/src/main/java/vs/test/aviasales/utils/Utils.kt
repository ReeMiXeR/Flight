package vs.test.aviasales.utils

import vs.test.aviasales.domain.model.Coordinates
import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.domain.model.Airport

val <T> T.exhaustive: T
    get() = this

inline fun <T1, T2> ifBothNotNull(arg1: T1?, arg2: T2?, block: (T1, T2) -> Unit): Unit? {
    return if (arg1 != null && arg2 != null) {
        block(arg1, arg2)
    } else null
}

val defaultRoute = Route(
    from = Airport(
        code = "SVO",
        locationFullName = "\u041c\u043e\u0441\u043a\u0432\u0430, \u0420\u043e\u0441\u0441\u0438\u044f",
        cityName = "\u041c\u043e\u0441\u043a\u0432\u0430",
        countryName = "Россия",
        coordinates = Coordinates(55.752041f, 37.617508f)
    ),
    to = Airport(
        code = "DEL",
        locationFullName = "\u0414\u0435\u043b\u0438, \u0418\u043d\u0434\u0438\u044f",
        countryName = "Индия",
        cityName = "\u0414\u0435\u043b\u0438",
        coordinates = Coordinates(28.632558f, 77.220036f)
    )
)
