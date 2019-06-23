package vs.test.aviasales.ui.viewmodel

import vs.test.aviasales.domain.model.Airport

abstract class SelectAirportViewModel<State, Event> : BaseViewModel<State, Event>() {
    abstract fun onQueryChanged(query: CharSequence)
    abstract fun onAirportClicked(item: Airport)
    abstract fun onCloseClicked()
}