package vs.test.aviasales.ui.model.route

import vs.test.aviasales.domain.model.Route

sealed class RouteState {
    data class ShowRoute(val route: Route) : RouteState()
}