package vs.test.aviasales.ui.model.select_airport

import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.ui.model.DataDiffResult

sealed class SelectAirportState {
    data class Content(val diff: DataDiffResult<List<Airport>>) : SelectAirportState()
    object Stub : SelectAirportState()
    object Empty : SelectAirportState()
    object Loading : SelectAirportState()
}