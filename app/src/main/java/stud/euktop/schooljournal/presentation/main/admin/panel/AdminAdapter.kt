package stud.euktop.schooljournal.presentation.main.admin.panel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.databinding.ItemAdminEntityBinding

class AdminAdapter<T : Any>(
    private val toText: (T) -> String,
    private val onEditClick: (T) -> Unit,
    private val onDeleteClick: (T) -> Unit
) : ListAdapter<T, AdminAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemAdminEntityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, toText(item))
        holder.binding.btnEdit.setOnClickListener { onEditClick(item) }
        holder.binding.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    class ViewHolder(val binding: ItemAdminEntityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Any, text: String) {
            binding.tvEntityName.text = text
        }
    }

    class Diff<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(p0: T & Any, p1: T & Any) = p0 == p1

        override fun areContentsTheSame(p0: T & Any, p1: T & Any): Boolean {
            return "$p0" == "$p1"
        }
    }
}