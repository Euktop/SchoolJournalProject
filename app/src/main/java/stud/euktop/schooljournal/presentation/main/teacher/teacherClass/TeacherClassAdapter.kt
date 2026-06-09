package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemTeacherClassBinding

class TeacherClassAdapter(
    private val onItemClick: (TeacherClassItem) -> Unit
) : ListAdapter<TeacherClassItem, TeacherClassAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeacherClassBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    class ViewHolder(
        private val binding: ItemTeacherClassBinding,
        private val onItemClick: (TeacherClassItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TeacherClassItem, position: Int) {
            val context = binding.root.context

            // Номер и буква класса
            binding.tvClassGrade.text = context.getString(
                stud.euktop.schooljournal.R.string.class_grade_letter_format,
                item.grade,
                item.letter
            )

            // Название предмета
            binding.tvSubjectName.text = item.subjectName

            // Кабинет (TODO: пока заглушка)
            binding.tvRoomName.text = item.roomName
                ?: context.getString(R.string.room_not_specified)

            // Цвет иконки класса (чередование)
            val bgDrawable = when (position % 3) {
                0 -> R.drawable.bg_class_icon_primary
                1 -> R.drawable.bg_class_icon_tertiary
                else -> R.drawable.bg_class_icon_secondary
            }
            binding.classIconContainer.setBackgroundResource(bgDrawable)

            // Клик
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<TeacherClassItem>() {
        override fun areItemsTheSame(old: TeacherClassItem, new: TeacherClassItem) =
            old.classId == new.classId

        override fun areContentsTheSame(old: TeacherClassItem, new: TeacherClassItem) =
            old == new
    }
}