package stud.euktop.schooljournal.presentation.main.student.homework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.utils.toBaseString
import stud.euktop.uikit.databinding.ItemStudentHomeworkBinding

class StudentHomeworkAdapter(
    private val onItemClick: (Homework) -> Unit
) : ListAdapter<Homework, StudentHomeworkAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemStudentHomeworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemStudentHomeworkBinding,
        private val onItemClick: (Homework) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Homework) {
            // заполнение полей, доступных в Homework
            binding.tvDescription.text = item.description
            binding.tvDate.text = item.createdAt.toBaseString()
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Homework>() {
        override fun areItemsTheSame(old: Homework, new: Homework) =
            old.homeworkId == new.homeworkId

        override fun areContentsTheSame(old: Homework, new: Homework) = old == new
    }
}