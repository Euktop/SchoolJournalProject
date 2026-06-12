package stud.euktop.schooljournal.presentation.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.utils.toBaseString
import stud.euktop.schooljournal.R
import stud.euktop.uikit.databinding.ItemHomeworkBinding

class HomeworkAdapter(
    private val onItemClick: (HomeworkFull) -> Unit,
    private val onMediaDownload: (Int) -> Unit,
    private val onMediaDelete: ((Int) -> Unit)? = null,
    private val isEditMode: Boolean = false
) : ListAdapter<HomeworkFull, HomeworkAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeworkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick, onMediaDownload, onMediaDelete, isEditMode)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemHomeworkBinding,
        private val onItemClick: (HomeworkFull) -> Unit,
        private val onMediaDownload: (Int) -> Unit,
        private val onMediaDelete: ((Int) -> Unit)?,
        private val isEditMode: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        private val mediaAdapter = HomeworkMediaAdapter(
            onDownloadClick = { media ->
                // Здесь нужно получить homeworkId из контекста
                // Для простоты передаем только mediaId
                onMediaDownload(media.mediaId)
            },
            onDeleteClick = if (isEditMode) { media ->
                onMediaDelete?.invoke(media.mediaId)
            } else null,
            isEditMode = isEditMode
        )

        fun bind(item: HomeworkFull) {
            binding.apply {
                tvSubjectName.text = item.lesson.subject.name
                tvClassName.text = "${item.lesson.classInfo.grade}${item.lesson.classInfo.letter}"
                tvLessonTopic.text = item.lesson.topic.takeIf { it.isNotBlank() }
                    ?: root.context.getString(R.string.theme_not_supported)
                tvDescription.text = item.description
                tvDate.text = item.createdAt.toBaseString()
                tvTeacher.text = "${item.createdBy.lastName} ${item.createdBy.firstName}"

                // Медиа-файлы
                if (item.media.isNotEmpty()) {
                    rvMedia.visibility = View.VISIBLE
                    rvMedia.layoutManager = LinearLayoutManager(root.context)
                    rvMedia.adapter = mediaAdapter
                    mediaAdapter.submitList(item.media)
                } else {
                    rvMedia.visibility = View.GONE
                }

                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<HomeworkFull>() {
        override fun areItemsTheSame(old: HomeworkFull, new: HomeworkFull) =
            old.homeworkId == new.homeworkId

        override fun areContentsTheSame(old: HomeworkFull, new: HomeworkFull) =
            old == new
    }
}