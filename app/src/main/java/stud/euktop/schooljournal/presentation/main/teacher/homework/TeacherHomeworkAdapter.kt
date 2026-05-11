package stud.euktop.schooljournal.presentation.main.teacher.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.utils.toBaseString
import stud.euktop.uikit.databinding.ItemTeacherHomeworkBinding

class TeacherHomeworkAdapter(
    private val onItemClick: (HomeworkFull) -> Unit
) : ListAdapter<HomeworkFull, TeacherHomeworkAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTeacherHomeworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTeacherHomeworkBinding,
        private val onItemClick: (HomeworkFull) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeworkFull) {
            binding.apply {
                tvSubjectName.text = item.lesson.subject.name
                tvClassName.text = "${item.lesson.classInfo.grade}${item.lesson.classInfo.letter}"
                tvDescription.text = item.description
                tvAttachedFiles.visibility = View.GONE  // пока нет файлов
                tvDate.text = item.createdAt.toBaseString()
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeworkFull>() {
        override fun areItemsTheSame(old: HomeworkFull, new: HomeworkFull) =
            old.homeworkId == new.homeworkId

        override fun areContentsTheSame(old: HomeworkFull, new: HomeworkFull) = old == new
    }
}