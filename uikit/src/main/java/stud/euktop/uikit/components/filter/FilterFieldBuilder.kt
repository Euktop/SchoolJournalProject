package stud.euktop.uikit.components.filter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.R
import stud.euktop.uikit.components.categories.SchJButtonCategories
import stud.euktop.uikit.components.datePicker.SchJDatePicker
import stud.euktop.uikit.components.input.SchJInput
import stud.euktop.uikit.components.input.select.content.SelectableAdapter
import stud.euktop.uikit.components.input.select.def.SchJSelect
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect
import stud.euktop.uikit.databinding.FilterDateRangeBinding
import stud.euktop.uikit.databinding.FilterSearchableSelectBinding
import stud.euktop.uikit.databinding.FilterSingleSelectBinding
import stud.euktop.uikit.databinding.FilterTextBinding
import stud.euktop.uikit.databinding.ItemListTextBinding
import stud.euktop.uikit.util.PxDpSp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FilterFieldBuilder {

    private class SimpleListAdapter<T : Any>(
        override val toText: (T?) -> String,
        override var onItemSelected: (T?) -> Unit
    ) : ListAdapter<T, SimpleListAdapter.ViewHolder>(DiffCallback()), SelectableAdapter<T> {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                ItemListTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.binding.root.text = toText(item)
            holder.binding.root.setOnClickListener { onItemSelected(item) }
        }

        class ViewHolder(val binding: ItemListTextBinding) : RecyclerView.ViewHolder(binding.root)

        class DiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
        }
    }

    fun addText(parent: LinearLayout, title: String, initialValue: String = ""): SchJInput {
        val binding = FilterTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val input = binding.inputText
        input.state = input.state.copy(text = initialValue, textHelper = title)
        parent.addView(binding.root)
        return input
    }

    data class SelectResult<T : Any>(
        val select: SchJSelect,
        val updateItems: (List<T>) -> Unit
    )

    fun <T : Any> addSingleSelect(
        parent: LinearLayout,
        fragmentManager: FragmentManager,
        title: String,
        items: List<T>,
        toText: (T?) -> String,
        onSelected: (T?) -> Unit,
        selectedItem: T? = null,
        onShowing: (() -> Unit)? = null
    ): SelectResult<T> {
        val binding =
            FilterSingleSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.tvText.text = title
        val select = binding.selectSingle
        val adapter = SimpleListAdapter(toText) { value ->
            onSelected(value)
            select.state = select.state.copy(selectText = toText(value))
        }
        adapter.submitList(items)
        select.attach(adapter, adapter, fragmentManager)
        select.onShowing = onShowing
        if (selectedItem != null) {
            select.state = select.state.copy(selectText = toText(selectedItem))
        }
        parent.addView(binding.root)
        return SelectResult(select) { newItems -> adapter.submitList(newItems) }
    }

    data class AddSearchableSelectResult<T : Any>(
        val select: SchJSearchableSelect,
        val updateItems: (List<T>) -> Unit
    )

    fun <T : Any> addSearchableSelect(
        parent: LinearLayout,
        fragmentManager: FragmentManager,
        title: String,
        items: List<T>,
        toText: (T?) -> String,
        onSelected: (T?) -> Unit,
        initialSelectedItem: T? = null,
        showFilterDialog: (() -> Unit)? = null,
        onShowing: (() -> Unit)? = null
    ): AddSearchableSelectResult<T> {
        val binding = FilterSearchableSelectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.tvTitle.text = title
        val select = binding.selectSearchable
        val adapter = SimpleListAdapter(toText) { value ->
            onSelected(value)
            select.state = select.state.copy(selectText = toText(value))
        }
        adapter.submitList(items)
        select.attach(adapter, adapter, fragmentManager, title, showFilterDialog)
        select.onShowing = onShowing
        if (initialSelectedItem != null) {
            select.state = select.state.copy(selectText = toText(initialSelectedItem))
        }
        parent.addView(binding.root)
        return AddSearchableSelectResult(select) { newItems -> adapter.submitList(newItems) }
    }

    data class DateRangeResult(
        val fromInput: SchJInput,
        val toInput: SchJInput,
        val dateFormatter: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    ) {
        private fun setTitle(input: SchJInput, date: Date?) {
            input.state =
                input.state.copy(text = date?.let {
                    dateFormatter.format(it)
                } ?: "")
        }

        fun setTitleTo(date: Date?) = setTitle(toInput, date)
        fun setTitleFrom(date: Date?) = setTitle(fromInput, date)
    }

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
                override fun afterTextChanged(s: Editable?) {
                    if (s?.toString().isNullOrEmpty()) onDateSelected(null)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            setOnClickListener {
                val picker = SchJDatePicker(context) { selected ->
                    onDateSelected(selected)
                    state = state.copy(text = dateFormat.format(selected))
                }
                picker.state = picker.state.copy(selectedDate = initialDate)
                dialogRef?.dismiss()
                dialogRef = picker
                picker.showUnique()
            }
        }

        val fromInput = binding.inputDateFrom.apply {
            state = state.copy(textHelper = title)
            fromDate?.let { state = state.copy(text = dateFormat.format(it)) }
        }
        fromInput.setupDatePicker(fromDate) { onFromDateSelected(it) }

        val toInput = binding.inputDateTo.apply {
            toDate?.let { state = state.copy(text = dateFormat.format(it)) }
        }
        toInput.setupDatePicker(toDate) { onToDateSelected(it) }

        parent.addView(binding.root)
        return DateRangeResult(fromInput, toInput, dateFormat)
    }

    fun <T : Any> addCategories(
        parent: ViewGroup,
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
            updateList(items.map { it.value to it.isSelected })
        }
        parent.addView(container)
        return categoriesView
    }
}