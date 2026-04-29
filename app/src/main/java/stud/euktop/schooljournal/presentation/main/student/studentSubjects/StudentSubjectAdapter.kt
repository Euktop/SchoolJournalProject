package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.StudentSubjectSummary
import stud.euktop.schooljournal.R
import stud.euktop.uikit.components.button.SchJButtonState
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
            binding.tvSubjectName.text = item.subjectName
            binding.tvAverageMark.text =
                binding.root.context.getString(
                    R.string.average_mark_format,
                    (item.averageMark ?: 0.0)
                )
            if (item.finalMark != null) {
                binding.btnFinalMark.visibility = android.view.View.VISIBLE
                binding.btnFinalMark.text = item.finalMark.toString()
                binding.btnFinalMark.state = SchJButtonState(
                    buttonType = SchJButtonState.ButtonType.CHIPS,
                    buttonClass = SchJButtonState.ButtonClass.PRIMARY
                )
            } else {
                binding.btnFinalMark.visibility = android.view.View.GONE
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<StudentSubjectSummary>() {
        override fun areItemsTheSame(old: StudentSubjectSummary, new: StudentSubjectSummary) =
            old.subjectId == new.subjectId

        override fun areContentsTheSame(old: StudentSubjectSummary, new: StudentSubjectSummary) =
            old == new
    }
}