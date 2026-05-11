package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.uikit.databinding.ItemAdminEntityBinding

class UsersListAdapter(
    private val onEditClick: (UserListItem) -> Unit,
    private val onDeleteClick: (UserListItem) -> Unit
) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    private var items: List<UserListItem> = emptyList()

    fun submitList(newItems: List<UserListItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvEntityName.text = "${item.lastName} ${item.firstName} (${item.email})"
        holder.binding.btnEdit.setOnClickListener { onEditClick(item) }
        holder.binding.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount() = items.size

    class ViewHolder(val binding: ItemAdminEntityBinding) : RecyclerView.ViewHolder(binding.root)
}