package stud.euktop.uikit.components.filter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import com.google.android.material.chip.Chip
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.databinding.LayoutFilterBarBinding

class SchJFilterBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutFilterBarBinding.inflate(LayoutInflater.from(context), this, true)

    var onSearchQueryChanged: (String) -> Unit = {}

    fun <T> setCategories(categories: ListSafe<Category<T>>) {
        binding.chipGroup.removeAllViews()
        categories.values.forEach { category ->
            val chip = Chip(context).apply {
                text = categories.toText(category)
                isCheckable = true
                isChecked = category.isSelected
                setOnCheckedChangeListener { _, isChecked ->
                    categories.onClick(category, isChecked)
                }
            }
            binding.chipGroup.addView(chip)
        }
        binding.chipGroup.visibility = if (categories.values.isEmpty()) GONE else VISIBLE
    }

    init {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?) =
                onSearchQueryChanged(newText ?: "").let { true }
        })
    }
}