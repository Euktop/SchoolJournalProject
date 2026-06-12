package stud.euktop.schooljournal.presentation.common.filter.audit

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.audit.AuditFilter
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.R
import stud.euktop.uikit.components.categories.SchJButtonCategories
import stud.euktop.uikit.components.filter.Category
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput

@AndroidEntryPoint
class AuditLogFilterDialog(
    initialFilter: AuditFilter,
    onFilterApplied: (AuditFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<AuditLogFilterViewModel, AuditFilter>(
    initialFilter, onFilterApplied, onError
) {
    override val setups: List<suspend () -> Unit>
        get() = emptyList()

    override val viewModel: AuditLogFilterViewModel by viewModels()

    private lateinit var userQueryInput: SchJInput
    private lateinit var tableNameInput: SchJInput
    private lateinit var actionTypesSelect: SchJButtonCategories
    private lateinit var dateRange: FilterFieldBuilder.DateRangeResult

    override fun setupFilterFields(container: LinearLayout) {
        userQueryInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(R.string.user_name_or_id),
            initialValue = initialFilter.userQuery ?: ""
        )

        tableNameInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(R.string.table_name),
            initialValue = initialFilter.tableName ?: ""
        )

        actionTypesSelect = FilterFieldBuilder.addCategories(
            parent = container,
            title = getString(R.string.action_types),
            items = getActionTypes(),
            toText = { it.name },
            onSelectionChanged = { value, isSelect ->
                initialFilter = initialFilter.copy(
                    actionTypes = initialFilter.actionTypes.toMutableList().apply {
                        if (isSelect) {
                            if (contains(value)) return@apply
                            add(value)
                        } else {
                            remove(value)
                        }
                    }.toSet()
                )
            }
        )

        dateRange = FilterFieldBuilder.addDateRange(
            parent = container,
            title = getString(R.string.date_range),
            fromDate = initialFilter.fromDate,
            toDate = initialFilter.toDate,
            onFromDateSelected = { date -> initialFilter = initialFilter.copy(fromDate = date) },
            onToDateSelected = { date -> initialFilter = initialFilter.copy(toDate = date) }
        )
    }

    private fun getActionTypes() =
        viewModel.loadActionTypes().map {
            Category(it, initialFilter.actionTypes.contains(it))
        }

    override fun resetFilters() {
        initialFilter = AuditFilter()

        userQueryInput.state = userQueryInput.state.copy(text = initialFilter.userQuery ?: "")
        tableNameInput.state = tableNameInput.state.copy(text = initialFilter.tableName ?: "")
        actionTypesSelect.updateListCategory(getActionTypes())
        dateRange.setTitleTo(initialFilter.toDate)
        dateRange.setTitleFrom(initialFilter.fromDate)
    }

    override fun collectFilter(): AuditFilter {
        return initialFilter.copy(
            userQuery = userQueryInput.state.text.takeIf { it.isNotBlank() },
            tableName = tableNameInput.state.text.takeIf { it.isNotBlank() },
        )
    }
}