package vs.test.aviasales.ui.model.route

import vs.test.aviasales.domain.model.Route

sealed class RouteEvent {
    data class SwitchToMapScreen(val route: Route) : RouteEvent()
}