package vs.test.aviasales.ui.viewmodel

import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.ui.model.Position

abstract class RouteViewModel<State, Event> : BaseViewModel<State, Event>() {
    abstract fun onSearchRouteClicked()
    abstract fun swap()
    abstract fun onAirportSelected(airport: Airport, position: Position)
}