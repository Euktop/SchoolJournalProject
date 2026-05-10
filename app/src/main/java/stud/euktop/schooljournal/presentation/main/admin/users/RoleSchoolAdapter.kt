package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.user.UserRole
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemRoleSchoolBinding
import stud.euktop.schooljournal.presentation.common.utils.toMessageId

class RoleSchoolAdapter(
    private val onDeleteClick: (UserRole) -> Unit
) : ListAdapter<UserRole, RoleSchoolAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRoleSchoolBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val role = getItem(position)
        holder.bind(role, onDeleteClick)
    }

    class ViewHolder(private val binding: ItemRoleSchoolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            role: UserRole,
            onDelete: (UserRole) -> Unit
        ) {
            val roleText = binding.root.context.getString(role.role.toMessageId())
            val schoolText = role.school?.name ?: binding.root.context.getString(R.string.no_school)
            binding.tvRoleSchool.text =
                binding.root.context.getString(R.string.role_school_format, roleText, schoolText)
            binding.btnDelete.setOnClickListener { onDelete(role) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UserRole>() {
        override fun areItemsTheSame(
            oldItem: UserRole,
            newItem: UserRole
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: UserRole,
            newItem: UserRole
        ): Boolean =
            oldItem == newItem
    }
}