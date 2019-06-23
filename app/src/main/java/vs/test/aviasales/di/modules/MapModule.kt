package vs.test.aviasales.di.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.domain.viewmodel.MapViewModelImpl
import vs.test.aviasales.ui.model.map.MapEvent
import vs.test.aviasales.ui.model.map.MapState
import vs.test.aviasales.ui.viewmodel.MapViewModel

val mapModule = module {
    viewModel<MapViewModel<MapState, MapEvent>> { (route: Route) -> MapViewModelImpl(route) }
}