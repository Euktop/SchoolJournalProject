package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemSubjectGradeBinding

class StudentSubjectGradeAdapter(
    private val onItemClick: (StudentSubjectGradeItem) -> Unit
) : ListAdapter<StudentSubjectGradeItem, StudentSubjectGradeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemSubjectGradeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemSubjectGradeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentSubjectGradeItem) {
            val context = binding.root.context

            binding.tvGrade.text = item.grade.toString()
            binding.tvWorkType.text = item.workType
            binding.tvTopic.text = context.getString(R.string.subject_detail_topic_format, item.topic)
            binding.tvDate.text = item.date
            binding.tvWeight.text = context.getString(R.string.subject_detail_weight_format, item.weight)

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<StudentSubjectGradeItem>() {
        override fun areItemsTheSame(old: StudentSubjectGradeItem, new: StudentSubjectGradeItem) =
            old.id == new.id

        override fun areContentsTheSame(old: StudentSubjectGradeItem, new: StudentSubjectGradeItem) =
            old == new
    }
}