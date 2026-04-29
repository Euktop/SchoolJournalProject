package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.AbsenceTypes
import stud.euktop.domain.model.StudentMarkItem
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.utils.toUI
import stud.euktop.uikit.components.button.SchJButtonState
import stud.euktop.uikit.databinding.ItemStudentMarkBinding

class LessonMarksAdapter(
    private val onItemClick: (StudentMarkItem) -> Unit
) : ListAdapter<StudentMarkItem, LessonMarksAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemStudentMarkBinding.inflate(
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

    class ViewHolder(private val binding: ItemStudentMarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StudentMarkItem) {
            binding.tvStudentName.text =
                binding.root.context.getString(
                    R.string.tv_student_name,
                    item.lastName,
                    item.firstName
                )
            val mark = binding.root.context.getString(
                item.absenceCode?.toUI()?.messageId ?: R.string.without_a_room_number
            )
            binding.btnMark.text = mark
            binding.btnMark.state = SchJButtonState(
                buttonType = SchJButtonState.ButtonType.CHIPS,
                buttonClass = when {
                    item.absenceCode == null -> SchJButtonState.ButtonClass.UNSELECT
                    else -> SchJButtonState.ButtonClass.SELECT
                }
            )
        }
    }

    class Diff : DiffUtil.ItemCallback<StudentMarkItem>() {
        override fun areItemsTheSame(old: StudentMarkItem, new: StudentMarkItem) =
            old.studentId == new.studentId

        override fun areContentsTheSame(old: StudentMarkItem, new: StudentMarkItem) =
            old == new
    }
}