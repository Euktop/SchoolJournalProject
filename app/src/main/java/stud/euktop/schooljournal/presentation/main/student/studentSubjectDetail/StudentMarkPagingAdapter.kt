package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemStudentSubjectMarkBinding
import java.text.SimpleDateFormat
import java.util.Locale

class StudentMarkPagingAdapter :
    PagingDataAdapter<StudentSubjectMark, StudentMarkPagingAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentSubjectMarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ItemStudentSubjectMarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentSubjectMark) {
            val context = binding.root.context

            val markValue = when (item.absenceCode) {
                AbsenceTypes.G2 -> context.getString(R.string.g2)
                AbsenceTypes.G3 -> context.getString(R.string.g3)
                AbsenceTypes.G4 -> context.getString(R.string.g4)
                AbsenceTypes.G5 -> context.getString(R.string.g5)
                AbsenceTypes.IRRESPECTABLE -> context.getString(R.string.irrespectable)
                AbsenceTypes.ILL -> context.getString(R.string.ill)
                AbsenceTypes.RESPECTABLE -> context.getString(R.string.respectable)
                else -> context.getString(R.string.absence_type_default)
            }
            binding.tvMarkValue.text = markValue

            val markColor =
                if (item.absenceCode == AbsenceTypes.G3 || item.absenceCode == AbsenceTypes.IRRESPECTABLE) {
                    ContextCompat.getColor(context, R.color.color_error)
                } else {
                    ContextCompat.getColor(context, R.color.color_text_accent)
                }
            binding.tvMarkValue.setTextColor(markColor)

            binding.tvWorkTitle.text = when (item.absenceCode) {
                AbsenceTypes.G5 -> context.getString(R.string.grade_excellent)
                AbsenceTypes.G4 -> context.getString(R.string.grade_good)
                else -> context.getString(R.string.grade_lesson_default)
            }

            binding.tvWorkTopic.text =
                item.comment ?: context.getString(R.string.topic_not_specified)

            val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(item.date)

            binding.tvWeight.text = context.getString(R.string.weight_format, 1.0)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<StudentSubjectMark>() {
            override fun areItemsTheSame(old: StudentSubjectMark, new: StudentSubjectMark) =
                old.gradeId == new.gradeId

            override fun areContentsTheSame(old: StudentSubjectMark, new: StudentSubjectMark) =
                old == new
        }
    }
}