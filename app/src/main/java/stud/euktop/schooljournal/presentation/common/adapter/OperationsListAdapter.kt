package stud.euktop.schooljournal.presentation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.databinding.ItemAdminEntityBinding

class OperationsListAdapter<T>(
    private val toText: (T) -> String,
    private val onEdit: (T) -> Unit,
    private val onDelete: (T) -> Unit,
    private val showContextMenu: Boolean = true,
    private val editOnClick: Boolean = false,
    diffCallback: DiffUtil.ItemCallback<T> = DefaultDiffCallback()
) : ListAdapter<T, OperationsListAdapter.ViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val binding =
            ItemAdminEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onEdit, onDelete, toText, showContextMenu, editOnClick)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder<T>(
        private val binding: ItemAdminEntityBinding,
        private val onEdit: (T) -> Unit,
        private val onDelete: (T) -> Unit,
        private val toText: (T) -> String,
        private val showContextMenu: Boolean,
        private val editOnClick: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentItem: T? = null

        fun bind(item: T) {
            currentItem = item
            binding.tvEntityName.text = toText(item)
            binding.btnEdit.setOnClickListener { onEdit(item) }
            binding.btnDelete.setOnClickListener { onDelete(item) }
            if (showContextMenu) {
                binding.root.setOnLongClickListener {
                    showContextMenuForItem(item)
                    true
                }
            }
            binding.root.setOnClickListener { if (editOnClick) onEdit(item) else Unit }
        }

        private fun showContextMenuForItem(item: T) {
            val context = binding.root.context
            val options = arrayOf("Редактировать", "Удалить")
            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(toText(item))
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> onEdit(item)
                        1 -> onDelete(item)
                    }
                }
                .show()
        }
    }

    private class DefaultDiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(p0: T & Any, p1: T & Any): Boolean {
            return p0 == p1
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(p0: T & Any, p1: T & Any): Boolean {
            return p0 == p1
        }
    }
}