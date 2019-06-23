package vs.test.aviasales.ui.viewmodel

import vs.test.aviasales.domain.model.Route

abstract class MapViewModel<State, Event>(
    protected val route: Route
) : BaseViewModel<State, Event>()