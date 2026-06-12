package stud.euktop.schooljournal.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.domain.model.homework.HomeworkMedia
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemMediaHomeworkBinding

class HomeworkMediaAdapter(
    private val onDownloadClick: (HomeworkMedia) -> Unit,
    private val onDeleteClick: ((HomeworkMedia) -> Unit)? = null,
    private val isEditMode: Boolean = false
) : ListAdapter<HomeworkMedia, HomeworkMediaAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMediaHomeworkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onDownloadClick, onDeleteClick, isEditMode)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemMediaHomeworkBinding,
        private val onDownloadClick: (HomeworkMedia) -> Unit,
        private val onDeleteClick: ((HomeworkMedia) -> Unit)?,
        private val isEditMode: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(media: HomeworkMedia) {
            binding.tvFileName.text = media.fileName
            binding.tvFileSize.text = formatFileSize(media.fileSize, media.contentType)

            // Иконка типа файла
            val iconRes = getFileIcon(media.contentType)
            binding.ivFileType.setImageResource(iconRes)

            // Действие кнопки
            if (isEditMode && onDeleteClick != null) {
                binding.btnAction.setImageResource(R.drawable.ic_close)
                binding.btnAction.setOnClickListener { onDeleteClick(media) }
            } else {
                binding.btnAction.setImageResource(R.drawable.ic_download)
                binding.btnAction.setOnClickListener { onDownloadClick(media) }
            }
        }

        private fun formatFileSize(sizeBytes: Int, contentType: String): String {
            val sizeMb = sizeBytes / (1024.0 * 1024.0)
            val sizeStr = if (sizeMb >= 1.0) {
                String.format("%.1f MB", sizeMb)
            } else {
                val sizeKb = sizeBytes / 1024.0
                String.format("%.1f KB", sizeKb)
            }
            val typeStr = contentType.substringAfter("/").uppercase()
            return "$sizeStr • $typeStr"
        }

        private fun getFileIcon(contentType: String): Int {
            return when {
                contentType.startsWith("image/") -> R.drawable.ic_image
                contentType.startsWith("video/") -> R.drawable.ic_video
                contentType.startsWith("audio/") -> R.drawable.ic_audio
                contentType.contains("pdf") -> R.drawable.ic_pdf
                else -> R.drawable.ic_description
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<HomeworkMedia>() {
        override fun areItemsTheSame(old: HomeworkMedia, new: HomeworkMedia) =
            old.mediaId == new.mediaId

        override fun areContentsTheSame(old: HomeworkMedia, new: HomeworkMedia) =
            old == new
    }
}