package stud.euktop.schooljournal.presentation.main.admin.audit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.audit.AuditLogListItem
import stud.euktop.uikit.databinding.ItemAdminEntityBinding
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AuditLogAdapter(
    private val onClick: (AuditLogListItem) -> Unit
) : PagingDataAdapter<AuditLogListItem, AuditLogAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminEntityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ItemAdminEntityBinding,
        private val onClick: (AuditLogListItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AuditLogListItem) {
            val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(item.eventTime)
            binding.tvEntityName.text = buildString {
                append(item.actionType)
                append(" | ")
                append(item.tableName)
                append(" | ")
                append(item.executorName)
                append(" | ")
                append(item.ipAddress)
                append(" | ")
                append(dateFormat.format(item.eventTime))
            }
            binding.root.setOnClickListener { onClick(item) }
            // скрываем кнопки редактирования/удаления, если они есть в layout
            binding.btnEdit.visibility = android.view.View.GONE
            binding.btnDelete.visibility = android.view.View.GONE
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<AuditLogListItem>() {
        override fun areItemsTheSame(old: AuditLogListItem, new: AuditLogListItem) =
            old.id == new.id

        override fun areContentsTheSame(old: AuditLogListItem, new: AuditLogListItem) =
            old == new
    }
}