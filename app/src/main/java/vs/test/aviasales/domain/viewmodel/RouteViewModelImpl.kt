package vs.test.aviasales.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.swap
import vs.test.aviasales.domain.repository.RouteRepository
import vs.test.aviasales.ui.model.Position
import vs.test.aviasales.ui.model.route.RouteEvent
import vs.test.aviasales.ui.model.route.RouteState
import vs.test.aviasales.ui.viewmodel.RouteViewModel
import vs.test.aviasales.utils.SingleLiveEvent

class RouteViewModelImpl(private val repository: RouteRepository) : RouteViewModel<RouteState, RouteEvent>() {

    private companion object {
        const val TAG = "RouteViewModelImpl"
    }

    override val state = MutableLiveData<RouteState>()
    override val events = SingleLiveEvent<RouteEvent>()

    init {
        safeSubscribe {
            repository.getRoutHistory()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    state.postValue(RouteState.ShowRoute(it))
                }, Timber.tag(TAG)::e)
        }
    }

    override fun onAirportSelectClicked(position: Position) {
        when (val value = state.value) {
            is RouteState.ShowRoute -> {
                events.postValue(
                    RouteEvent.SwitchToSelectAirportScreen(value.route, position)
                )
            }
        }
    }

    override fun onSearchRouteClicked() {
        when (val value = state.value) {
            is RouteState.ShowRoute -> {
                events.postValue(
                    RouteEvent.SwitchToMapScreen(value.route)
                )
            }
        }
    }

    override fun swap() {
        when (val value = state.value) {
            is RouteState.ShowRoute -> {
                value.route.swap().also {
                    state.postValue(RouteState.ShowRoute(it))
                    repository.insertRouteHistory(it)
                }
            }
        }
    }

    override fun onAirportSelected(airport: Airport, position: Position) {
        when (val value = state.value) {
            is RouteState.ShowRoute -> {
                value.route.copy(
                    from = when (position) {
                        Position.FROM -> airport
                        Position.TO -> value.route.from
                    },
                    to = when (position) {
                        Position.FROM -> value.route.to
                        Position.TO -> airport
                    }
                ).also {
                    state.postValue(RouteState.ShowRoute(it))
                    repository.insertRouteHistory(it)
                }
            }
        }
    }
}