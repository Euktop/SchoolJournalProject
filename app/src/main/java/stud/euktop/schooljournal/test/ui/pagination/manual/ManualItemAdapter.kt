package stud.euktop.schooljournal.test.ui.pagination.manual

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ManualItemAdapter : RecyclerView.Adapter<ManualItemAdapter.ViewHolder>() {

    private val items = mutableListOf<String>()

    fun submitList(newItems: List<String>, isClear: Boolean = false) {
        if (isClear) {
            items.clear()
        }
        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    fun addLoadingFooter() {
        items.add("__loading__")
        notifyItemInserted(items.size - 1)
    }

    fun removeLoadingFooter() {
        if (items.isNotEmpty() && items.last() == "__loading__") {
            items.removeAt(items.size - 1)
            notifyItemRemoved(items.size)
        }
    }

    fun getRealItemCount(): Int = items.count { it != "__loading__" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(32, 32, 32, 32)
            textSize = 18f
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = if (item == "__loading__") "Загрузка..." else item
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}