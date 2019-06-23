package vs.test.aviasales.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import vs.test.aviasales.R
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.Bezier
import vs.test.aviasales.domain.model.Route
import vs.test.aviasales.domain.model.toLatLng
import vs.test.aviasales.ui.model.map.MapEvent
import vs.test.aviasales.ui.model.map.MapState
import vs.test.aviasales.ui.viewmodel.MapViewModel
import vs.test.aviasales.utils.exhaustive
import kotlin.math.abs
import kotlin.math.min

class MapActivity : BaseActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "MapActivity"

        // intent keys
        private const val KEY_ROUTE = "$TAG.route"
        private const val KEY_ANIMATION_START_VALUE = "$TAG.animation_start_value"
        private const val KEY_PLANE_ROTATION = "$TAG.rotation"

        // map constants
        private const val CENTER_ANCHOR = 0.5f
        private const val PLANE_ANGLE_CALCULATION_STEP = 0.003f

        private const val ANIMATION_DURATION_MILLIS = 10_000L

        fun getIntent(context: Context, route: Route): Intent {
            return Intent(context, MapActivity::class.java).apply {
                putExtra(KEY_ROUTE, route)
            }
        }
    }

    private val route by lazy { intent.getSerializableExtra(KEY_ROUTE) as Route }
    private val viewModel: MapViewModel<MapState, MapEvent> by viewModel { parametersOf(route) }
    private var animationStartValue = 0f
    private var planeRotation = 0f
    private var animator: Animator? = null
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat(KEY_ANIMATION_START_VALUE, animationStartValue)
        outState.putFloat(KEY_PLANE_ROTATION, planeRotation)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        animationStartValue = savedInstanceState?.getFloat(KEY_ANIMATION_START_VALUE) ?: 0f
        planeRotation = savedInstanceState?.getFloat(KEY_PLANE_ROTATION) ?: 0f
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        map.uiSettings.apply {
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
        }

        val (from, to) = route

        // Создаем маркеры городов и добавляем их на карту
        map.addMarker(from.toCityMarker())
        map.addMarker(to.toCityMarker())

        observeState(map)
    }

    override fun onPause() {
        super.onPause()
        animator?.cancel()
        dialog?.dismiss()
    }

    private fun observeState(map: GoogleMap) {
        viewModel.state.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is MapState.ShowMap -> {
                    map.apply {
                        addPolyline(it.polyline.color(ContextCompat.getColor(this@MapActivity, R.color.map_dot_color)))
                        moveCamera(it.bounds)
                        startAnimation(it.curve, it.plane, this)
                    }
                }
            }.exhaustive
        })
    }

    private fun startAnimation(curve: Bezier, planeOptions: MarkerOptions, map: GoogleMap) {
        val planeMarker = map.addMarker(planeOptions.rotation(planeRotation))
        val (from, to) = animationStartValue to 1f

        animator = ValueAnimator.ofFloat(from, to).apply {
            duration = (ANIMATION_DURATION_MILLIS * (1 - animationStartValue)).toLong()
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                animationStartValue = value
                val currPoint = curve.calculate(value).toLatLng()
                val nextPoint = curve.calculate(min(value + PLANE_ANGLE_CALCULATION_STEP, 1f)).toLatLng()

                with(planeMarker) {
                    position = currPoint
                    rotation = getAngle(currPoint, nextPoint, rotation).also { rotation ->
                        planeRotation = rotation
                    }
                }

            }
            addListener(onEnd = {
                dialog = AlertDialog.Builder(this@MapActivity)
                    .setTitle(getString(R.string.map_dialog_title))
                    .setMessage(getString(R.string.map_dialog_description))
                    .setPositiveButton(R.string.map_dialog_yse_button) { dialog, which -> this@MapActivity.finish() }
                    .show()
            })

            start()
        }
    }

    private fun View.convertToBitmap(): Bitmap {
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED).let {
            measure(it, it)
        }
        layout(0, 0, measuredWidth, measuredHeight)
        val r = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(r)
        background?.draw(canvas) ?: r.eraseColor(Color.TRANSPARENT)
        draw(canvas)
        return r
    }

    private fun Airport.toCityMarker(): MarkerOptions {
        val markerView = TextView(this@MapActivity, null, 0, R.style.MapCityPin).apply {
            text = code
        }

        return MarkerOptions()
            .position(coordinates.toLatLng())
            .anchor(
                CENTER_ANCHOR,
                CENTER_ANCHOR
            )
            .icon(BitmapDescriptorFactory.fromBitmap(markerView.convertToBitmap()))
    }

    private fun getAngle(begin: LatLng, end: LatLng, fallbackValue: Float): Float {
        val lat = abs(begin.latitude - end.latitude)
        val lon = abs(begin.longitude - end.longitude)

        return when {
            lat == lon && lon == 0.0 -> fallbackValue
            begin.latitude < end.latitude && begin.longitude < end.longitude -> Math.toDegrees(Math.atan(lon / lat)).toFloat()
            begin.latitude >= end.latitude && begin.longitude < end.longitude -> (90 - Math.toDegrees(Math.atan(lon / lat)) + 90).toFloat()
            begin.latitude >= end.latitude && begin.longitude >= end.longitude -> (Math.toDegrees(Math.atan(lon / lat)) + 180).toFloat()
            begin.latitude < end.latitude && begin.longitude >= end.longitude -> (90 - Math.toDegrees(Math.atan(lon / lat)) + 270).toFloat()
            else -> fallbackValue
        }
    }
}



