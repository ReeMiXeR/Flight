package vs.test.aviasales.ui.adapter.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vs.test.aviasales.ui.adapter.DisplayableItem

abstract class Adapter<T : DisplayableItem, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var items: List<T> = emptyList()

    abstract val itemResourceId: Int

    abstract fun createHolder(view: View): VH

    abstract fun bind(holder: VH, item: T, position: Int)

    final override fun getItemCount() = items.size

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createHolder(LayoutInflater.from(parent.context).inflate(itemResourceId, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bind(holder, items[position], position)
    }

    fun set(newItems: List<T>, diffResult: DiffUtil.DiffResult) {
        this.items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }
}