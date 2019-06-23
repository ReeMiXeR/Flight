package vs.test.aviasales.ui.model.route

import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.ui.model.Position

sealed class RouteEvent {
    data class SwitchToSelectAirportScreen(val route: Route, val position: Position) : RouteEvent()
    data class SwitchToMapScreen(val route: Route) : RouteEvent()
}