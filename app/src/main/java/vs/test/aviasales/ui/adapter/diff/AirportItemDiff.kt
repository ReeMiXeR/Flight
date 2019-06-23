package vs.test.aviasales.ui.adapter.diff

import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.ui.adapter.DisplayableItem


class AirportItemDiff(
    oldItems: List<DisplayableItem>,
    newItems: List<DisplayableItem>
) : AdapterItemDiffUtilsCallback<DisplayableItem>(oldItems, newItems) {
    override fun sameItems(newItem: DisplayableItem, oldItem: DisplayableItem): Boolean {
        return newItem is Airport
                && oldItem is Airport
                && newItem.code.equals(oldItem.code, false)
    }

    override fun sameContent(newItem: DisplayableItem, oldItem: DisplayableItem): Boolean {
        return newItem == oldItem
    }
}