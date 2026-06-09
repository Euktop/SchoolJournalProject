package stud.euktop.schooljournal.presentation.main.teacher.lessonMarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.ItemChipBinding

class ChipAdapter(
    private val items: List<String>,
    private var selectedIndex: Int = 0,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<ChipAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemChipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String, isSelected: Boolean) {
            binding.root.text = text
            val bgRes = if (isSelected) R.drawable.bg_chip_active else R.drawable.bg_chip_inactive
            val textRes =
                if (isSelected) R.color.color_text_on_accent else R.color.color_text_secondary

            binding.root.background = ContextCompat.getDrawable(binding.root.context, bgRes)
            binding.root.setTextColor(ContextCompat.getColor(binding.root.context, textRes))

            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) onClick(pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == selectedIndex)
    }

    override fun getItemCount() = items.size

    fun select(index: Int) {
        val oldIndex = selectedIndex
        selectedIndex = index
        notifyItemChanged(oldIndex)
        notifyItemChanged(index)
    }
}