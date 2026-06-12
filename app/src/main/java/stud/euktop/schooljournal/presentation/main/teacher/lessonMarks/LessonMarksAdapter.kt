package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.attendance.StudentMarkItem
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemStudentMarkBinding

class LessonMarksAdapter(
    private val onItemClick: (StudentMarkItem) -> Unit,
    private val onEditClick: ((StudentMarkItem) -> Unit)? = null
) : ListAdapter<StudentMarkItem, LessonMarksAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemStudentMarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
        holder.binding.btnEdit.visibility = onEditClick?.let {
            holder.binding.btnEdit.setOnClickListener { it(item) }
            return@let View.VISIBLE
        } ?: View.GONE
    }

    class ViewHolder(val binding: ItemStudentMarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StudentMarkItem) {
            val context = binding.root.context

            // Инициалы
            val initials =
                "${item.lastName.firstOrNull() ?: ""}${item.firstName.firstOrNull() ?: ""}".uppercase()
            binding.tvInitials.text = initials

            // Имя
            binding.tvStudentName.text = "${item.lastName} ${item.firstName.firstOrNull()}."

            // Бейдж оценки
            val (text, bgColor, textColor) = getGradeAppearance(item.absenceCode, context)
            binding.tvGradeBadge.text = text
            binding.tvGradeBadge.setTextColor(textColor)

            val drawable = ContextCompat.getDrawable(context, R.drawable.bg_grade_badge)
                ?.mutate() as? GradientDrawable
            drawable?.setColor(bgColor)
            binding.tvGradeBadge.background = drawable
        }

        private fun getGradeAppearance(
            type: AbsenceTypes?, context: android.content.Context
        ): Triple<String, Int, Int> {
            return when (type) {
                AbsenceTypes.G5 -> Triple(
                    "5",
                    ContextCompat.getColor(context, R.color.color_grade_5_bg),
                    ContextCompat.getColor(context, R.color.color_grade_5_text)
                )

                AbsenceTypes.G4 -> Triple(
                    "4",
                    ContextCompat.getColor(context, R.color.color_grade_4_bg),
                    ContextCompat.getColor(context, R.color.color_grade_4_text)
                )

                AbsenceTypes.G3 -> Triple(
                    "3",
                    ContextCompat.getColor(context, R.color.color_grade_3_bg),
                    ContextCompat.getColor(context, R.color.color_grade_3_text)
                )

                AbsenceTypes.G2 -> Triple(
                    "2",
                    ContextCompat.getColor(context, R.color.color_grade_2_bg),
                    ContextCompat.getColor(context, R.color.color_grade_2_text)
                )

                AbsenceTypes.IRRESPECTABLE, AbsenceTypes.ILL, AbsenceTypes.RESPECTABLE -> Triple(
                    "н",
                    ContextCompat.getColor(context, R.color.color_grade_absent_bg),
                    ContextCompat.getColor(context, R.color.color_grade_absent_text)
                )

                null -> Triple(
                    "-",
                    ContextCompat.getColor(context, R.color.color_grade_empty_bg),
                    ContextCompat.getColor(context, R.color.color_grade_empty_text)
                )
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<StudentMarkItem>() {
        override fun areItemsTheSame(old: StudentMarkItem, new: StudentMarkItem) =
            old.studentId == new.studentId

        override fun areContentsTheSame(old: StudentMarkItem, new: StudentMarkItem) = old == new
    }
}