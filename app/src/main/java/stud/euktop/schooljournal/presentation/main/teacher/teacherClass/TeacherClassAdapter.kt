package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.lesson.TeacherClassItem
import stud.euktop.schooljournal.R
import stud.euktop.uikit.databinding.ItemTeacherClassBinding

class TeacherClassAdapter(
    private val onItemClick: (TeacherClassItem) -> Unit
) : ListAdapter<TeacherClassItem, TeacherClassAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemTeacherClassBinding.inflate(
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

    class ViewHolder(binding: ItemTeacherClassBinding) : RecyclerView.ViewHolder(binding.root) {
        val classGrade = binding.tvClassGrade
        val subjectName = binding.tvSubjectName
        fun bind(item: TeacherClassItem) {
            classGrade.text = itemView.context.getString(
                R.string.class_grade_letter_format,
                item.grade,
                item.letter
            )
            subjectName.text = item.subjectName
        }
    }

    class Diff : DiffUtil.ItemCallback<TeacherClassItem>() {
        override fun areItemsTheSame(old: TeacherClassItem, new: TeacherClassItem) =
            old.classId == new.classId

        override fun areContentsTheSame(old: TeacherClassItem, new: TeacherClassItem) = old == new
    }
}