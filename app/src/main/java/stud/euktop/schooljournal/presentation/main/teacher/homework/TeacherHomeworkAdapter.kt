package stud.euktop.schooljournal.presentation.main.teacher.homework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.homework.Homework
import stud.euktop.domain.utils.toBaseString
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemTeacherHomeworkBinding

class TeacherHomeworkAdapter(
    private val onItemClick: (Homework) -> Unit
) : ListAdapter<Homework, TeacherHomeworkAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeacherHomeworkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTeacherHomeworkBinding,
        private val onItemClick: (Homework) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(homework: Homework) {
            binding.apply {
                tvSubjectName.text = homework.lesson.subject.name
                tvClassName.text = homework.lesson.classInfo.name
                tvDescription.text = homework.description
                tvAttachedFiles.text =
                    homework.attachedFiles?.let { "${binding.root.context.getString(R.string.files)}: $it" }
                        ?: ""
                tvDate.text = homework.createdAt.toBaseString()
                root.setOnClickListener { onItemClick(homework) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Homework>() {
        override fun areItemsTheSame(oldItem: Homework, newItem: Homework): Boolean =
            oldItem.homeworkId == newItem.homeworkId

        override fun areContentsTheSame(oldItem: Homework, newItem: Homework): Boolean =
            oldItem == newItem
    }
}