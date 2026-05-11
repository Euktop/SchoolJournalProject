package stud.euktop.schooljournal.presentation.main.admin.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import stud.euktop.uikit.databinding.ItemRoleSchoolBinding

class RoleSchoolAdapter(
    private val onDelete: (RoleWithSchool) -> Unit
) : ListAdapter<RoleWithSchool, RoleSchoolAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRoleSchoolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemRoleSchoolBinding,
        private val onDelete: (RoleWithSchool) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(role: RoleWithSchool) {
            val roleText = binding.root.context.getString(role.role.toMessageId())
            val schoolText =
                role.schoolName ?: binding.root.context.getString(R.string.without_a_room_number)
            binding.tvRoleSchool.text = "$roleText ($schoolText)"
            binding.btnDelete.setOnClickListener { onDelete(role) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<RoleWithSchool>() {
        override fun areItemsTheSame(old: RoleWithSchool, new: RoleWithSchool) =
            old.role == new.role && old.schoolId == new.schoolId

        override fun areContentsTheSame(old: RoleWithSchool, new: RoleWithSchool) = old == new
    }
}