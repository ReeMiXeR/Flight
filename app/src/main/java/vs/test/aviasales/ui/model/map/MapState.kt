package vs.test.aviasales.ui.model.map

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import vs.test.aviasales.domain.model.Bezier

sealed class MapState {
    data class ShowMap(
        val curve: Bezier,
        val polyline: PolylineOptions,
        val bounds: CameraUpdate,
        val plane: MarkerOptions
    ) : MapState()
}