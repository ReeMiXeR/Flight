package vs.test.aviasales.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_route.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vs.test.aviasales.R
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.ui.MotionTransition
import vs.test.aviasales.ui.model.Position
import vs.test.aviasales.ui.model.route.RouteEvent
import vs.test.aviasales.ui.model.route.RouteState
import vs.test.aviasales.ui.scaleTap
import vs.test.aviasales.ui.singleTouch
import vs.test.aviasales.ui.viewmodel.RouteViewModel
import vs.test.aviasales.utils.exhaustive

class RouteActivity : BaseActivity() {

    companion object {
        private const val TAG = "RouteActivity"
    }

    private val viewModel by viewModel<RouteViewModel<RouteState, RouteEvent>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        initViews()
        observeEvents()
        observeState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == SelectAirportActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null -> {
                val airport = data.getSerializableExtra(SelectAirportActivity.KEY_SELECTED_AIRPORT) as? Airport
                val position = data.getSerializableExtra(SelectAirportActivity.KEY_POSITION) as? Position
                if (airport != null && position != null) {
                    viewModel.onAirportSelected(airport, position)
                } else {
                    Timber.tag(TAG).e(IllegalStateException("${SelectAirportActivity.KEY_SELECTED_AIRPORT} not found"))
                }
            }
        }
    }

    private fun observeState() {
        viewModel.state.observe(this, Observer {
            when (it) {
                is RouteState.ShowRoute -> {
                    val (from, to) = it.route

                    route_from_code.text = if (isPointsSwapped()) to.code else from.code
                    route_from_name.text = if (isPointsSwapped()) to.cityName else from.cityName
                    route_from_country.text = if (isPointsSwapped()) to.countryName else from.countryName

                    route_to_code.text = if (isPointsSwapped()) from.code else to.code
                    route_to_name.text = if (isPointsSwapped()) from.cityName else to.cityName
                    route_to_country.text = if (isPointsSwapped()) from.countryName else to.countryName
                }
            }.exhaustive
        })
    }

    private fun observeEvents() {
        viewModel.events.observe(this, Observer {
            when (it) {
                is RouteEvent.SwitchToMapScreen -> {
                    startActivity(MapActivity.getIntent(this, it.route))
                }

                is RouteEvent.SwitchToSelectAirportScreen -> {
                    startActivityForResult(
                        SelectAirportActivity.getIntent(this, it.route, it.position),
                        SelectAirportActivity.REQUEST_CODE
                    )
                }
            }.exhaustive
        })
    }

    private fun initViews() {
        route_swap_points.scaleTap()

        listOf(
            route_from_code,
            route_from_name,
            route_from_country
        ).forEach {
            it.setOnClickListener {
                singleTouch {
                    viewModel.onAirportSelectClicked(if (isPointsSwapped()) Position.FROM else Position.TO)
                }
            }
        }

        listOf(
            route_to_code,
            route_to_name,
            route_to_country
        ).forEach {
            it.setOnClickListener {
                singleTouch {
                    viewModel.onAirportSelectClicked(if (isPointsSwapped()) Position.FROM else Position.TO)
                }
            }
        }

        route_container.setTransitionListener(
            MotionTransition(
                onStart = viewModel::swap
            )
        )

        route_search_button.setOnClickListener {
            singleTouch {
                viewModel.onSearchRouteClicked()
            }
        }
    }

    private fun isPointsSwapped(): Boolean = with(route_container) {
        startState == currentState
    }.not()
}