package vs.test.aviasales.data.repository

import io.reactivex.Single
import vs.test.aviasales.data.storage.PersistentStorage
import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.domain.repository.RouteRepository

class RouteRepositoryImpl(private val persistentStorage: PersistentStorage) : RouteRepository {

    override fun getRoutHistory(): Single<Route> {
        return persistentStorage.getRouteCodesHistory()
            .flatMap {
                persistentStorage.getRouteByCodes(it)
            }
    }

    override fun insertRouteHistory(route: Route) {
        persistentStorage.insertRouteHistory(route)
    }
}