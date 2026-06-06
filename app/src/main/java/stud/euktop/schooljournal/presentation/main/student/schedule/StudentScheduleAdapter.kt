package stud.euktop.schooljournal.presentation.main.student.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.schedule.StudentScheduleItem
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.databinding.ItemStudentScheduleBinding

class StudentScheduleAdapter(
    private val onItemClick: (StudentScheduleItem) -> Unit
) : ListAdapter<StudentScheduleItem, StudentScheduleAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentScheduleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemStudentScheduleBinding,
        private val onItemClick: (StudentScheduleItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StudentScheduleItem) {
            binding.tvDate.text = item.date.toBaseString()
            binding.tvTime.text = "${item.startTime} - ${item.endTime}"
            binding.tvSubject.text = item.subjectName
            binding.tvTopic.text = item.topic
            binding.tvTeacher.text = item.teacherFullName
            binding.tvRoom.text = item.roomName ?: item.locationAddress ?: "Не указано"
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<StudentScheduleItem>() {
        override fun areItemsTheSame(old: StudentScheduleItem, new: StudentScheduleItem) =
            old.lessonId == new.lessonId

        override fun areContentsTheSame(old: StudentScheduleItem, new: StudentScheduleItem) =
            old == new
    }
}