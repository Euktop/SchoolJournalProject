package stud.euktop.schooljournal.presentation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.components.input.select.content.SelectableAdapter
import stud.euktop.uikit.databinding.ItemListTextBinding

class PagingSelectAdapter<T : Any>(
    override val toText: (T?) -> String,
    override var onItemSelected: (T?) -> Unit,
    diffCallback: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
) : PagingDataAdapter<T, PagingSelectAdapter.ViewHolder>(diffCallback), SelectableAdapter<T> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.root.text = toText(item)
        holder.binding.root.setOnClickListener { onItemSelected(item) }
    }

    class ViewHolder(val binding: ItemListTextBinding) : RecyclerView.ViewHolder(binding.root)
}