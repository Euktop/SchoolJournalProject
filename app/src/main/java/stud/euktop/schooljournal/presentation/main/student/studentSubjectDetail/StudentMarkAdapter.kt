package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.StudentSubjectMark
import stud.euktop.uikit.components.button.SchJButtonState
import stud.euktop.uikit.databinding.ItemStudentSubjectMarkBinding

class StudentMarkAdapter(
    private val onItemClick: (StudentSubjectMark) -> Unit
) : ListAdapter<StudentSubjectMark, StudentMarkAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemStudentSubjectMarkBinding.inflate(
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

    class ViewHolder(private val binding: ItemStudentSubjectMarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StudentSubjectMark) {
            binding.tvDate.text = item.date
            val markText = when {
                item.value != null -> item.value.toString()
                item.absenceCode != null -> item.absenceCode?.uppercase()
                else -> "—"
            }
            binding.btnMark.text = markText
            binding.btnMark.state = SchJButtonState(
                buttonType = SchJButtonState.ButtonType.CHIPS,
                buttonClass = when {
                    item.absenceCode != null -> SchJButtonState.ButtonClass.UNSELECT
                    item.value == 5 || item.value == 4 -> SchJButtonState.ButtonClass.PRIMARY
                    item.value == 3 -> SchJButtonState.ButtonClass.PRIMARY
                    else -> SchJButtonState.ButtonClass.UNSELECT
                }
            )
            binding.tvComment.text = item.comment ?: ""
        }
    }

    class Diff : DiffUtil.ItemCallback<StudentSubjectMark>() {
        override fun areItemsTheSame(old: StudentSubjectMark, new: StudentSubjectMark) =
            old.date == new.date

        override fun areContentsTheSame(old: StudentSubjectMark, new: StudentSubjectMark) =
            old == new
    }
}