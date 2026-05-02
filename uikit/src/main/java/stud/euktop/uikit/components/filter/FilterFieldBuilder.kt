package stud.euktop.uikit.components.filter

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import stud.euktop.uikit.R
import stud.euktop.uikit.components.categories.SchJButtonCategories
import stud.euktop.uikit.components.datePicker.SchJDatePicker
import stud.euktop.uikit.components.input.SchJInput
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.def.SchJSelect
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect
import stud.euktop.uikit.databinding.FilterDateRangeBinding
import stud.euktop.uikit.databinding.FilterSearchableSelectBinding
import stud.euktop.uikit.databinding.FilterSingleSelectBinding
import stud.euktop.uikit.databinding.FilterTextBinding
import stud.euktop.uikit.util.PxDpSp
import java.text.SimpleDateFormat
import java.util.*

object FilterFieldBuilder {

    /** Текстовое поле (поиск) */
    fun addText(
        parent: LinearLayout,
        title: String,
        initialValue: String = ""
    ): SchJInput {
        val binding = FilterTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val input = binding.inputText
        input.state = input.state.copy(text = initialValue, textHelper = title)
        parent.addView(binding.root)
        return input
    }


    data class SelectResult<T : Any>(
        val select: SchJSelect,
        val register: SchJSelect.RegisterList<T>
    )

    /** Обычный выпадающий список (SchJSelect) */
    fun <T : Any> addSingleSelect(
        parent: LinearLayout,
        fragmentManager: FragmentManager,
        title: String,
        items: ListSafe<T>,
        selectedItem: T? = null,
        onShowing: (() -> Unit)? = null,
    ): SelectResult<T> {
        val binding =
            FilterSingleSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.tvText.text = title
        val register = binding.selectSingle.RegisterList(items).apply { register(fragmentManager) }
        binding.selectSingle.onShowing = onShowing
        if (selectedItem != null) {
            binding.selectSingle.state =
                binding.selectSingle.state.copy(selectText = items.toText(selectedItem))
        }
        parent.addView(binding.root)
        return SelectResult(select = binding.selectSingle, register = register)
    }


    /** Результат добавления поискового селекта */
    data class AddSearchableSelectResult<T : Any>(
        val select: SchJSearchableSelect,
        val register: SchJSearchableSelect.RegisterList<T>
    )

    /** Поисковый выпадающий список (SchJSearchableSelect) с локальной фильтрацией */

    fun <T : Any> addSearchableSelect(
        parent: LinearLayout,
        fragmentManager: FragmentManager,
        title: String,
        items: ListSafe<T> = ListSafe(),
        initialSelectedItem: T? = null,
        showFilterDialog: (() -> Unit)? = null,
        onShowing: (() -> Unit)? = null,
    ): AddSearchableSelectResult<T> {
        val binding = FilterSearchableSelectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.tvTitle.text = title
        val select = binding.selectSearchable
        fun updateText(v: T) {
            select.state = select.state.copy(selectText = items.toText(v))
        }

        val register =
            select.RegisterList(items, showFilterDialog)
                .apply {
                    register(fragmentManager)
                }
        select.onShowing = onShowing

        if (initialSelectedItem != null) {
            updateText(initialSelectedItem)
        }
        parent.addView(binding.root)
        return AddSearchableSelectResult(select, register)
    }

    /** Результат добавления диапазона дат */
    data class DateRangeResult(
        val fromInput: SchJInput,
        val toInput: SchJInput
    )

    /** Диапазон дат (два поля с календариком) */
    fun addDateRange(
        parent: LinearLayout,
        title: String,
        dateFormat: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()),
        fromDate: Date? = null,
        toDate: Date? = null,
        onFromDateSelected: (Date?) -> Unit = {},
        onToDateSelected: (Date?) -> Unit = {}
    ): DateRangeResult {
        val binding =
            FilterDateRangeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        var dialogRef: DatePickerDialog? = null

        fun SchJInput.setupDatePicker(initialDate: Date?, onDateSelected: (Date?) -> Unit) {
            editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.toString().isNullOrEmpty())
                        onDateSelected(null)
                }

                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                }

            })
            setOnClickListener {
                val datePicker = SchJDatePicker(context) { selectedDate ->
                    onDateSelected(selectedDate)
                    state = state.copy(text = dateFormat.format(selectedDate))
                }
                datePicker.state = datePicker.state.copy(selectedDate = initialDate)
                if (dialogRef?.isShowing == true) dialogRef?.dismiss()
                dialogRef = datePicker
                datePicker.showUnique()
            }
        }

        val fromInput = binding.inputDateFrom.apply {
            state = state.copy(textHelper = title)
            if (fromDate != null) state = state.copy(text = dateFormat.format(fromDate))
        }
        fromInput.setupDatePicker(fromDate) { onFromDateSelected(it) }

        val toInput = binding.inputDateTo.apply {
            if (toDate != null) state = state.copy(text = dateFormat.format(toDate))
        }
        toInput.setupDatePicker(toDate) { onToDateSelected(it) }

        parent.addView(binding.root)
        return DateRangeResult(fromInput, toInput)
    }

    fun <T : Any> addCategories(
        parent: LinearLayout,
        title: String,
        items: List<Category<T>>,
        toText: (T) -> String,
        onSelectionChanged: (T, Boolean) -> Unit
    ): SchJButtonCategories {
        val container = LinearLayout(parent.context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        val titleView = TextView(parent.context).apply {
            text = title
            setTextAppearance(R.style.TextAppearance_SchJ_Caption_Medium)
            setTextColor(ContextCompat.getColor(parent.context, R.color.color_text_secondary))
            setPadding(0, 0, 0, PxDpSp.dp(4f).px.toInt())
        }
        container.addView(titleView)
        val categoriesView = SchJButtonCategories(parent.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            initAdapter(
                toText = toText,
                onClick = onSelectionChanged
            )
            (adapter as? SchJButtonCategories.Adapter<Category<T>, T>)?.submitList(
                items.map { Category(it.value, it.isSelected) }
            )
        }
        parent.addView(container)
        return categoriesView
    }
}