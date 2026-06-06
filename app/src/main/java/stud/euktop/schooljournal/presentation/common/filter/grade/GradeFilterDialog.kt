package stud.euktop.schooljournal.presentation.common.filter.grade

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.baseDateFormat
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail.GradeFilter
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import java.util.Date

@AndroidEntryPoint
class GradeFilterDialog(
    initialFilter: GradeFilter,
    onFilterApplied: (GradeFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<GradeFilterViewModel, GradeFilter>(initialFilter, onFilterApplied, onError) {

    override val viewModel: GradeFilterViewModel by viewModels()

    private lateinit var dateRangeResult: FilterFieldBuilder.DateRangeResult
    private var fromDate: Date? = initialFilter.startDate
    private var toDate: Date? = initialFilter.endDate

    override val setups: List<suspend () -> Unit> = emptyList()

    override fun setupFilterFields(container: LinearLayout) {
        dateRangeResult = FilterFieldBuilder.addDateRange(
            parent = container,
            title = getString(R.string.period),
            dateFormat = baseDateFormat,
            fromDate = fromDate,
            toDate = toDate,
            onFromDateSelected = { date -> fromDate = date },
            onToDateSelected = { date -> toDate = date }
        )
    }

    override fun resetFilters() {
        dateRangeResult.fromInput.state = dateRangeResult.fromInput.state.copy(text = "")
        dateRangeResult.toInput.state = dateRangeResult.toInput.state.copy(text = "")
        fromDate = null
        toDate = null
        initialFilter = GradeFilter()
    }

    override fun collectFilter(): GradeFilter = initialFilter.copy(
        startDate = fromDate,
        endDate = toDate
    )
}