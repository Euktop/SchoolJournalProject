package stud.euktop.schooljournal.presentation.common.filter.lesson

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.R
import stud.euktop.uikit.components.filter.FilterFieldBuilder

@AndroidEntryPoint
class LessonFilterDialog(
    initialFilter: AppLessonFilter,
    onFilterApplied: (AppLessonFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<LessonFilterViewModel, AppLessonFilter>(
    initialFilter, onFilterApplied, onError
) {

    override val viewModel: LessonFilterViewModel by viewModels()
    private lateinit var dateRange: FilterFieldBuilder.DateRangeResult

    override val setups: List<suspend () -> Unit> = emptyList()

    override fun setupFilterFields(container: LinearLayout) {
        dateRange = FilterFieldBuilder.addDateRange(
            parent = container,
            title = getString(R.string.date_range),
            fromDate = initialFilter.dateFrom,
            toDate = initialFilter.dateTo,
            onFromDateSelected = { dateFrom ->
                initialFilter = initialFilter.copy(dateFrom = dateFrom)
            },
            onToDateSelected = { dateTo ->
                initialFilter = initialFilter.copy(dateTo = dateTo)
            }
        )
    }

    override fun resetFilters() {
        dateRange.fromInput.state = dateRange.fromInput.state.copy(text = "")
        dateRange.toInput.state = dateRange.toInput.state.copy(text = "")
        initialFilter = AppLessonFilter()
    }

    override fun collectFilter(): AppLessonFilter = initialFilter
}