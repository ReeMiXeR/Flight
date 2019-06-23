package vs.test.aviasales.data.model.db

import androidx.room.ColumnInfo
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class DBCoordinates(
    @ColumnInfo(name = "coordinates_lat")
    val lat: Float,
    @ColumnInfo(name = "coordinates_lon")
    val lon: Float
)