package vs.test.aviasales.di.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import vs.test.aviasales.data.repository.RouteRepositoryImpl
import vs.test.aviasales.domain.repository.RouteRepository
import vs.test.aviasales.domain.viewmodel.RouteViewModelImpl
import vs.test.aviasales.ui.model.route.RouteEvent
import vs.test.aviasales.ui.model.route.RouteState
import vs.test.aviasales.ui.viewmodel.RouteViewModel

val routeModule = module {
    factory<RouteRepository> { RouteRepositoryImpl(get()) }
    viewModel<RouteViewModel<RouteState, RouteEvent>> { RouteViewModelImpl(get()) }
}