package vs.test.aviasales.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import rx.Single
import rx.schedulers.Schedulers
import vs.test.aviasales.R
import vs.test.aviasales.domain.model.*
import vs.test.aviasales.ui.activity.MapActivity
import vs.test.aviasales.ui.model.map.MapEvent
import vs.test.aviasales.ui.model.map.MapState
import vs.test.aviasales.ui.viewmodel.MapViewModel
import vs.test.aviasales.utils.SingleLiveEvent
import java.util.*
import kotlin.math.min

class MapViewModelImpl(
    route: Route
) : MapViewModel<MapState, MapEvent>(route) {

    private companion object {
        private const val CENTER_ANCHOR = 0.5f
        private const val DEFAULT_BOUNDS_PADDING = 100
        private const val PLANE_Z_INDEX = 2f
        private const val POLYLINE_DOT_SIZE = 15f
        private const val PATTERN_GAP_LENGTH_PX = 10
        private const val CURVE_GENERATOR_STEP = 0.00005f
    }

    override val state = MutableLiveData<MapState>()
    override val events = SingleLiveEvent<MapEvent>()

    init {
        Single.fromCallable {
            val startPoint = route.from.coordinates
            val endPoint = route.to.coordinates

            getCurve(startPoint, endPoint).let {
                MapState.ShowMap(
                    curve = it,
                    polyline = produceRouteLine(it),
                    bounds = produceMapBounds(startPoint, endPoint, DEFAULT_BOUNDS_PADDING),
                    plane = producePlane()
                )
            }
        }.subscribeOn(Schedulers.computation())
            .subscribe {
                state.postValue(it)
            }
    }

    private fun getCurve(
        startPoint: Coordinates,
        endPoint: Coordinates
    ): Bezier = Bezier(
        start = startPoint.lat and startPoint.lon,
        end = endPoint.lat and endPoint.lon,
        control1 = (startPoint to endPoint).toControl1(),
        control2 = (startPoint to endPoint).toControl2()
    )

    private fun produceRouteLine(curve: Bezier): PolylineOptions {
        val curvePoints = mutableListOf<LatLng>()
        var progress = 0f
        while (progress <= 1f) {
            curvePoints.add(
                curve.calculate(min(progress, 1f)).toLatLng()
            )
            progress += CURVE_GENERATOR_STEP
        }

        val polylinePattern = Arrays.asList(
            Gap(PATTERN_GAP_LENGTH_PX.toFloat()),
            Dot()
        )

        return PolylineOptions()
            .add(*curvePoints.toTypedArray())
            .width(POLYLINE_DOT_SIZE)
            .pattern(polylinePattern)
    }

    private fun produceMapBounds(startPoint: Coordinates, endPoint: Coordinates, bounds: Int): CameraUpdate {
        return CameraUpdateFactory.newLatLngBounds(
            LatLngBounds.builder()
                .include(startPoint.toLatLng())
                .include(endPoint.toLatLng())
                .build(),
            bounds
        )
    }

    private fun producePlane(): MarkerOptions {
        return MarkerOptions()
            .position(route.from.coordinates.toLatLng())
            .anchor(CENTER_ANCHOR, CENTER_ANCHOR)
            .zIndex(PLANE_Z_INDEX)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
    }
}