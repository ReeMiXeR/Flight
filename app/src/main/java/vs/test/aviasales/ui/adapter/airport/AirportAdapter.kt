package vs.test.aviasales.ui.adapter.airport

import android.view.View
import vs.test.aviasales.R
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.ui.adapter.base.Adapter

typealias OnAirportClicked = (Airport) -> Unit

class AirportAdapter(private val onClick: OnAirportClicked) : Adapter<Airport, AirportViewHolder>() {

    override val itemResourceId: Int
        get() = R.layout.item_airport

    override fun createHolder(view: View): AirportViewHolder = AirportViewHolder(view)

    override fun bind(holder: AirportViewHolder, item: Airport, position: Int) {
        holder.apply {
            view.setOnClickListener { onClick.invoke(item) }
            fullName.text = item.locationFullName
            code.text = item.code
        }
    }
}