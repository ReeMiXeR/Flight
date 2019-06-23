package vs.test.aviasales.data.model.db

import android.content.ContentValues
import androidx.room.*


@Entity(tableName = "airport", indices = [Index("airport_code", unique = true)])
class DBAirport(
    @ColumnInfo(name = "airport_code")
    val code: String,
    @ColumnInfo(name = "airport_location_full_name")
    val locationFullName: String,
    @ColumnInfo(name = "airport_city_name")
    val cityName: String,
    @ColumnInfo(name = "airport_country_name")
    val countryName: String,
    @Embedded
    val coordinates: DBCoordinates
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "airport_id")
    var id: Int = 0

    fun toInsertField(): Triple<String, Int, ContentValues> {
        return Triple(
            "airport",
            OnConflictStrategy.REPLACE,
            ContentValues().apply {
                put("airport_code", code)
                put("airport_location_full_name", locationFullName)
                put("airport_city_name", cityName)
                put("airport_country_name", countryName)
                put("coordinates_lat", coordinates.lat)
                put("coordinates_lon", coordinates.lon)
            }
        )
    }
}