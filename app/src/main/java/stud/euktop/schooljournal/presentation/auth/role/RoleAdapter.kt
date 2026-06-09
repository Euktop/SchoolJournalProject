package stud.euktop.schooljournal.presentation.auth.role

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.user.Role
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemRoleBinding

class RoleAdapter(
    private val onRoleSelected: (Role) -> Unit
) : ListAdapter<RoleItem, RoleAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onRoleSelected)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemRoleBinding,
        private val onRoleSelected: (Role) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RoleItem) {
            val context = binding.root.context

            binding.tvRoleTitle.text = context.getString(item.titleRes)
            binding.tvRoleDescription.text = context.getString(item.descriptionRes)

            binding.ivRoleIcon.setImageResource(item.iconRes)
            if (item.isSelected) {
                binding.cardRole.setBackgroundResource(R.drawable.bg_role_card_selected)
                binding.iconContainer.setBackgroundResource(R.drawable.bg_role_card_selected_icon)
                binding.ivCheck.visibility = View.VISIBLE
                binding.ivRoleIcon.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.color_text_on_accent
                    )
                )
                binding.tvRoleDescription.apply {
                    setPadding(
                        paddingLeft,
                        paddingTop,
                        paddingRight,
                        lineHeight
                    )
                }
            } else {
                binding.cardRole.setBackgroundResource(R.drawable.bg_role_card_unselected)
                binding.iconContainer.setBackgroundResource(R.drawable.bg_role_card_unselected)
                binding.ivCheck.visibility = View.INVISIBLE
                binding.ivRoleIcon.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.color_text_primary
                    )
                )
                binding.tvRoleDescription.apply {
                    setPadding(
                        paddingLeft,
                        paddingTop,
                        paddingRight,
                        0
                    )
                }
            }

            binding.root.setOnClickListener {
                onRoleSelected(item.role)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<RoleItem>() {
        override fun areItemsTheSame(oldItem: RoleItem, newItem: RoleItem): Boolean {
            return oldItem.role == newItem.role
        }

        override fun areContentsTheSame(oldItem: RoleItem, newItem: RoleItem): Boolean {
            return oldItem == newItem
        }
    }
}