package stud.euktop.uikit.components.input.select.searchable

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import stud.euktop.uikit.components.adapter.SchJTextAdapter
import stud.euktop.uikit.databinding.DialogSearchableSelectBinding

class SchJSearchableSelectDialog<T, C>(
    private val config: SchJSearchableSelectDialogConfig<T, C>
) : DialogFragment() {

    private var adapter: SchJTextAdapter<T>? = null
    private lateinit var binding: DialogSearchableSelectBinding

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        binding = DialogSearchableSelectBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        setupViews()
        return AlertDialog.Builder(requireContext()).setView(binding.root).create()
    }

    private fun setupViews() {
        binding.toolbar.apply {
            title = config.title
            setNavigationOnClickListener { dismiss() }
        }
        adapter = SchJTextAdapter(object : SchJTextAdapter.Listener<T> {
            override fun onClick(value: T) {
                config.items.onClick(value, true)
                dismiss()
            }

            override fun toText(value: T): String = config.items.toText(value)
            override fun isEquals(value1: T, value2: T): Boolean = value1 == value2
        })
        binding.recyclerView.adapter = adapter
        updateItems(config.items.values)
        updateCategories(config.categories.values)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                config.onSearchQueryChanged(newText ?: "")
                return true
            }
        })
    }

    /**
     * Обновляет список элементов (вызывается из ViewModel после загрузки новых данных).
     */
    fun updateItems(newItems: List<T>) {
        adapter?.submitList(newItems) {
            binding.recyclerView.update()
        }
    }

    fun updateCategories(newCategory: List<C>) {
        if (newCategory.isEmpty()) {
            binding.chipGroupCategories.visibility = View.GONE
            return
        }
        binding.chipGroupCategories.apply {
            visibility = View.VISIBLE
            removeAllViews()
            newCategory.forEach { category ->
                val chip = Chip(requireContext()).apply {
                    text = config.categories.toText(category)
                    isCheckable = true
                    setOnCheckedChangeListener { _, isChecked ->
                        config.categories.onClick(category, isChecked)
                    }
                }
                addView(chip)
            }
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}