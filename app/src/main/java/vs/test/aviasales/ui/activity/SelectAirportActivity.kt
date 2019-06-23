package vs.test.aviasales.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.android.synthetic.main.activity_select_airport.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import vs.test.aviasales.R
import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.ui.adapter.airport.AirportAdapter
import vs.test.aviasales.ui.model.Position
import vs.test.aviasales.ui.model.select_airport.SelectAirportEvent
import vs.test.aviasales.ui.model.select_airport.SelectAirportState
import vs.test.aviasales.ui.viewmodel.SelectAirportViewModel
import vs.test.aviasales.utils.exhaustive
import java.util.concurrent.TimeUnit


class SelectAirportActivity : BaseActivity() {

    companion object {
        private const val TAG = "SelectAirportActivity"

        const val REQUEST_CODE = 69
        const val KEY_SELECTED_AIRPORT = "$TAG.selected_airport"
        const val KEY_POSITION = "$TAG.position"
        const val KEY_ROUTE = "$TAG.current_route"

        const val TEXT_INPUT_DELAY = 300L

        private const val DRAWABLE_END = 2
        private const val DRAWABLE_END_SIZE_FACTOR = 2f
        private const val DRAWABLE_START = 0
        private const val DRAWABLE_START_SIZE_FACTOR = 1.3f

        fun getIntent(context: Context, route: Route, position: Position): Intent {
            return Intent(context, SelectAirportActivity::class.java).apply {
                putExtra(KEY_POSITION, position)
                putExtra(KEY_ROUTE, route)
            }
        }
    }

    private val currentRoute by lazy { intent.getSerializableExtra(KEY_ROUTE) as Route }
    private val rvAdapter by lazy { AirportAdapter(viewModel::onAirportClicked) }
    private val viewModel: SelectAirportViewModel<SelectAirportState, SelectAirportEvent> by viewModel {
        parametersOf(currentRoute)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_airport)
        initViews()
        observeState()
        observeEvents()
    }

    override fun onBackPressed() {
        viewModel.onCloseClicked()
    }

    private fun initViews() {
        select_airport_rv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(this@SelectAirportActivity)
            itemAnimator = null
        }

        safeSubscribe {
            select_airport_query
                .textChanges()
                .doOnNext { setupEditTextIcons(it.isBlank()) }
                .debounce(TEXT_INPUT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(viewModel::onQueryChanged, Timber.tag(TAG)::e)
        }


        select_airport_query.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = select_airport_query.compoundDrawables[DRAWABLE_END]
                val drawableStart = select_airport_query.compoundDrawables[DRAWABLE_START]
                if (drawableEnd != null && event.rawX >= (select_airport_query.right - drawableEnd.bounds.width() * DRAWABLE_END_SIZE_FACTOR)) {
                    select_airport_query.text?.clear()
                    return@setOnTouchListener true
                }
                if (drawableStart != null && event.rawX <= (select_airport_query.left + drawableStart.bounds.width() * DRAWABLE_START_SIZE_FACTOR)) {
                    viewModel.onCloseClicked()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun setupEditTextIcons(isEmpty: Boolean) {
        select_airport_query.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_left_arrow,
            0,
            if (isEmpty) 0 else R.drawable.ic_clear,
            0
        )
    }

    private fun observeEvents() {
        viewModel.events
            .observe(this, Observer {
                when (it) {
                    is SelectAirportEvent.OnAirportSelected -> {
                        setResult(
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(KEY_SELECTED_AIRPORT, it.airport)
                                putExtra(KEY_POSITION, intent.getSerializableExtra(KEY_POSITION))
                            }
                        )
                        finish()
                    }

                    is SelectAirportEvent.Close -> {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }

                    is SelectAirportEvent.AirportAlreadySelectedDialog -> {
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.select_airport_dialog_title))
                            .setMessage(getString(R.string.select_airport_dialog_description))
                            .setPositiveButton(android.R.string.yes) { dialog, which -> }
                            .show()
                        Unit
                    }
                }.exhaustive
            })
    }

    private fun observeState() {
        viewModel.state
            .observe(this, Observer<SelectAirportState> { state ->
                when (state) {
                    is SelectAirportState.Content -> {
                        select_airport_container.showContent()
                        state.diff.result?.let {
                            rvAdapter.set(state.diff.data, it)
                        }
                    }

                    is SelectAirportState.Empty -> {
                        select_airport_container.showEmpty()
                    }

                    is SelectAirportState.Stub -> {
                        select_airport_container.showStub()
                    }

                    is SelectAirportState.Loading -> {
                        select_airport_container.showLoading()
                    }
                }.exhaustive
            })
    }
}