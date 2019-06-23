package vs.test.aviasales.domain.repository

import io.reactivex.Single
import vs.test.aviasales.domain.model.Route

interface RouteRepository {
    fun insertRouteHistory(route: Route)
    fun getRoutHistory(): Single<Route>
}