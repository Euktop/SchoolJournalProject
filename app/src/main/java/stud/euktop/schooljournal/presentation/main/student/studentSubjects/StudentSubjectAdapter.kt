package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.schooljournal.presentation.common.utils.SubjectIconProvider
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemStudentSubjectBinding

class StudentSubjectAdapter(private val onItemClick: (StudentSubjectSummary) -> Unit) :
    ListAdapter<StudentSubjectSummary, StudentSubjectAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemStudentSubjectBinding.inflate(
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

    class ViewHolder(private val binding: ItemStudentSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentSubjectSummary) {
            val context = binding.root.context

            // Название и преподаватель
            binding.tvSubjectName.text = item.subjectName
            binding.tvTeacherName.text = item.teacherName?.let {
                context.getString(R.string.teacher_format, it)
            } ?: context.getString(R.string.teacher_not_specified)

            binding.ivSubjectIcon.setImageResource(
                SubjectIconProvider.getIcon(item.subjectId)
            )

            // Бейдж среднего балла
            binding.tvAverageMark.text = item.averageMark?.let { String.format("%.1f", it) } ?: "—"

            // Итоговая оценка
            if (item.finalMark != null) {
                binding.tvFinalMark.visibility = View.VISIBLE
                binding.tvFinalMark.text = context.getString(R.string.result_format, item.finalMark)
            } else {
                binding.tvFinalMark.visibility = View.GONE
            }

            // Прогресс-бар
            val progress = ((item.averageMark ?: 0.0) / 5.0 * 100).toInt()
            binding.progressBar.progress = progress
        }
    }

    class Diff : DiffUtil.ItemCallback<StudentSubjectSummary>() {
        override fun areItemsTheSame(old: StudentSubjectSummary, new: StudentSubjectSummary) =
            old.subjectId == new.subjectId

        override fun areContentsTheSame(old: StudentSubjectSummary, new: StudentSubjectSummary) =
            old == new
    }
}