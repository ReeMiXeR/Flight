package vs.test.aviasales.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil

abstract class AdapterItemDiffUtilsCallback<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>
) : DiffUtil.Callback() {

    final override fun getOldListSize(): Int = oldItems.size

    final override fun getNewListSize(): Int = newItems.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return sameItems(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return sameContent(oldItem, newItem)
    }

    abstract fun sameItems(newItem: T, oldItem: T): Boolean


    abstract fun sameContent(newItem: T, oldItem: T): Boolean
}