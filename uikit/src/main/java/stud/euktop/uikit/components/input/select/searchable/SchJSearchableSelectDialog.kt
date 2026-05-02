package stud.euktop.uikit.components.input.select.searchable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import stud.euktop.uikit.R
import stud.euktop.uikit.components.adapter.SchJTextAdapter
import stud.euktop.uikit.components.bottomsheet.SchJBottomSheet
import stud.euktop.uikit.databinding.DialogSearchableSelectBinding

class SchJSearchableSelectDialog<T>(
    private val config: SchJSearchableSelectDialogConfig<T>
) : SchJBottomSheet() {

    private var adapter: SchJTextAdapter<T>? = null
    private lateinit var binding: DialogSearchableSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchableSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    private fun setupViews() {
        binding.toolbar.apply {
            title = config.title
            setNavigationOnClickListener { dismiss() }
            showFilterDialog = config.showFilterDialog
            menu?.findItem(R.id.action_filter)?.isVisible = config.showFilterDialog != null
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
        binding.toolbar.showFilterDialog = config.showFilterDialog
        updateConfig(config.items.values)
    }

    fun updateConfig(newItems: List<T>) {
        adapter?.submitList(newItems) {
            binding.recyclerView.update()
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }
}