package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.TeacherLessonItem
import stud.euktop.schooljournal.R
import stud.euktop.uikit.databinding.ItemTeacherLessonBinding

class TeacherLessonsAdapter(
    private val onItemClick: (TeacherLessonItem) -> Unit
) : ListAdapter<TeacherLessonItem, TeacherLessonsAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemTeacherLessonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemTeacherLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TeacherLessonItem) {
            binding.tvLessonTopic.text =
                item.topic ?: binding.root.context.getString(R.string.without_a_theme)
            binding.tvLessonDate.text = item.date
            binding.tvLessonTime.text =
                binding.root.context.getString(
                    R.string.tv_lessons_time,
                    item.startTime,
                    item.endTime
                )
            binding.tvLessonRoom.text =
                item.roomName ?: binding.root.context.getString(R.string.without_a_room_number)
            binding.tvLessonTeacher.text = item.teacherName
        }
    }

    class Diff : DiffUtil.ItemCallback<TeacherLessonItem>() {
        override fun areItemsTheSame(old: TeacherLessonItem, new: TeacherLessonItem) =
            old.lessonId == new.lessonId

        override fun areContentsTheSame(old: TeacherLessonItem, new: TeacherLessonItem) =
            old == new
    }
}