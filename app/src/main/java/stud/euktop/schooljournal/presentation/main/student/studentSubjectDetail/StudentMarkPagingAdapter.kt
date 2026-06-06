// presentation/main/student/studentSubjectDetail/StudentMarkPagingAdapter.kt
package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.presentation.common.utils.getTextFromId
import stud.euktop.schooljournal.presentation.common.utils.toUI
import stud.euktop.uikit.components.button.SchJButtonState
import stud.euktop.uikit.databinding.ItemStudentSubjectMarkBinding

class StudentMarkPagingAdapter(
    private val onItemClick: (StudentSubjectMark) -> Unit
) : PagingDataAdapter<StudentSubjectMark, StudentMarkPagingAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentSubjectMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ItemStudentSubjectMarkBinding,
        private val onItemClick: (StudentSubjectMark) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StudentSubjectMark) {
            binding.tvDate.text = item.date.toBaseString()
            val markText = getTextFromId(binding.root.resources, item.absenceCode?.toUI()?.messageId)
            binding.btnMark.text = markText
            binding.btnMark.state = SchJButtonState(
                buttonType = SchJButtonState.ButtonType.CHIPS,
                buttonClass = if (item.absenceCode != null) SchJButtonState.ButtonClass.SELECT else SchJButtonState.ButtonClass.UNSELECT
            )
            binding.tvComment.text = item.comment ?: ""
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StudentSubjectMark>() {
            override fun areItemsTheSame(old: StudentSubjectMark, new: StudentSubjectMark) = old.gradeId == new.gradeId
            override fun areContentsTheSame(old: StudentSubjectMark, new: StudentSubjectMark) = old == new
        }
    }
}