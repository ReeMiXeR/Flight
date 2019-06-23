package vs.test.aviasales.ui.model.select_airport

import vs.test.aviasales.domain.model.Airport

sealed class SelectAirportEvent {
    data class OnAirportSelected(val airport: Airport) : SelectAirportEvent()
    object Close : SelectAirportEvent()
    object AirportAlreadySelectedDialog : SelectAirportEvent()
}