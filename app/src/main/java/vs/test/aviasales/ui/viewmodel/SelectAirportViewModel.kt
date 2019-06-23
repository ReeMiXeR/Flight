package vs.test.aviasales.ui.viewmodel

import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.Route

abstract class SelectAirportViewModel<State, Event>(protected val currentRoute: Route) : BaseViewModel<State, Event>() {
    abstract fun onQueryChanged(query: CharSequence)
    abstract fun onAirportClicked(item: Airport)
    abstract fun onCloseClicked()
}