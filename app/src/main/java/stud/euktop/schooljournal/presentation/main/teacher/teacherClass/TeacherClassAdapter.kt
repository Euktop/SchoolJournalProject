package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.schooljournal.R
import stud.euktop.uikit.databinding.ItemTeacherClassBinding

class TeacherClassAdapter(
    private val onItemClick: (TeacherClassItem) -> Unit
) : ListAdapter<TeacherClassItem, TeacherClassAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTeacherClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    class ViewHolder(private val binding: ItemTeacherClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TeacherClassItem) {
            binding.tvClassGrade.text = binding.root.context.getString(
                R.string.class_grade_letter_format,
                item.grade,
                item.letter
            )
            binding.tvSubjectName.text = item.subjectName
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<TeacherClassItem>() {
        override fun areItemsTheSame(old: TeacherClassItem, new: TeacherClassItem) =
            old.classId == new.classId

        override fun areContentsTheSame(old: TeacherClassItem, new: TeacherClassItem) = old == new
    }
}