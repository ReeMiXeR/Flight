package vs.test.aviasales.ui.adapter.airport

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.item_airport.view.*
import vs.test.aviasales.ui.adapter.base.BaseViewHolder

class AirportViewHolder(val view: View) : BaseViewHolder(view) {
    val fullName: AppCompatTextView = view.airport_full_name
    val code: AppCompatTextView = view.airport_code
}