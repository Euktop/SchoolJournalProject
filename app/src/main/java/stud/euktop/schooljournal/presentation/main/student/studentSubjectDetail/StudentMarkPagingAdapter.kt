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

class StudentMarkPagingAdapter(
    private val onItemClick: (StudentSubjectMark) -> Unit
) : PagingDataAdapter<StudentSubjectMark, StudentMarkPagingAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentSubjectMarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
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
            val context = binding.root.context

            // Оценка
            val markValue = when (item.absenceCode) {
                AbsenceTypes.G2 -> "2"
                AbsenceTypes.G3 -> "3"
                AbsenceTypes.G4 -> "4"
                AbsenceTypes.G5 -> "5"
                AbsenceTypes.IRRESPECTABLE -> "Н"
                AbsenceTypes.ILL -> "Б"
                AbsenceTypes.RESPECTABLE -> "У"
                else -> "-"
            }
            binding.tvMarkValue.text = markValue

            // Цвет оценки (Третичный для 3 и Н, Акцент для остальных)
            val markColor =
                if (item.absenceCode == AbsenceTypes.G3 || item.absenceCode == AbsenceTypes.IRRESPECTABLE) {
                    ContextCompat.getColor(context, R.color.color_error) // Или tertiary
                } else {
                    ContextCompat.getColor(context, R.color.color_text_accent)
                }
            binding.tvMarkValue.setTextColor(markColor)

            // Название работы (Пока заглушка, так как в DTO нет типа работы)
            binding.tvWorkTitle.text = when (item.absenceCode) {
                AbsenceTypes.G5 -> "Отличная работа"
                AbsenceTypes.G4 -> "Хорошая работа"
                else -> "Оценка за урок"
            }

            // Тема (Используем comment как тему, если он есть)
            binding.tvWorkTopic.text = item.comment ?: "Тема не указана"

            // Дата (Формат "24 Окт")
            val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(item.date)

            // Вес (Пока заглушка, так как в DTO нет веса)
            binding.tvWeight.text = context.getString(R.string.weight_format, "1.0")

            binding.root.setOnClickListener { onItemClick(item) }
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