package vs.test.aviasales.di.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import vs.test.aviasales.data.repository.SuggestRepositoryImpl
import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.domain.repository.SuggestRepository
import vs.test.aviasales.domain.viewmodel.SelectAirportViewModelImpl
import vs.test.aviasales.ui.model.select_airport.SelectAirportEvent
import vs.test.aviasales.ui.model.select_airport.SelectAirportState
import vs.test.aviasales.ui.viewmodel.SelectAirportViewModel

val selectAirportModule = module {
    factory<SuggestRepository> { SuggestRepositoryImpl(get(), get()) }
    viewModel<SelectAirportViewModel<SelectAirportState, SelectAirportEvent>> { (route: Route) -> SelectAirportViewModelImpl(get(), route) }
}