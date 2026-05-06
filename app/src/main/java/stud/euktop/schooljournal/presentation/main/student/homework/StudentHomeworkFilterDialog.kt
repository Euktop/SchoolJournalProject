package stud.euktop.schooljournal.presentation.main.student.homework

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.homework.HomeworkFilter2
import stud.euktop.domain.model.school.Subject
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.select.ListSafe
import java.util.Date

@AndroidEntryPoint
class StudentHomeworkFilterDialog(
    initialFilter: HomeworkFilter2,
    onFilterApplied: (HomeworkFilter2) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<StudentHomeworkFilterViewModel, HomeworkFilter2>(
    initialFilter, onFilterApplied, onError
) {

    override val viewModel: StudentHomeworkFilterViewModel by viewModels()

    private lateinit var subjectSelectResult: FilterFieldBuilder.AddSearchableSelectResult<Subject>
    private lateinit var dateRangeResult: FilterFieldBuilder.DateRangeResult
    private var selectedSubject: Subject? = null
    private var dateFrom: Date? = initialFilter.createdFrom
    private var dateTo: Date? = initialFilter.createdTo

    override val setups: List<suspend () -> Unit> = listOf { setupSubjects() }

    private suspend fun setupSubjects() {
        viewModel.subjects.collect { subjects ->
            subjectSelectResult.register.updateItems(subjects)
        }
    }

    override fun setupFilterFields(container: LinearLayout) {
        subjectSelectResult = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.subject),
            items = ListSafe(
                values = emptyList(),
                toText = { it?.name ?: "" },
                onClick = { subject, _ -> selectedSubject = subject }
            ),
            onShowing = { viewModel.loadSubjects() },
            initialSelectedItem = null
        )

        dateRangeResult = FilterFieldBuilder.addDateRange(
            parent = container,
            title = getString(R.string.date_create),
            fromDate = dateFrom,
            toDate = dateTo,
            onFromDateSelected = { date -> dateFrom = date },
            onToDateSelected = { date -> dateTo = date }
        )
    }

    override fun resetFilters() {
        subjectSelectResult.select.state = subjectSelectResult.select.state.copy(selectText = "")
        dateRangeResult.fromInput.state = dateRangeResult.fromInput.state.copy(text = "")
        dateRangeResult.toInput.state = dateRangeResult.toInput.state.copy(text = "")
        selectedSubject = null
        dateFrom = null
        dateTo = null
    }

    override fun collectFilter(): HomeworkFilter2 {
        return HomeworkFilter2(
            subjectId = selectedSubject?.subjectId,
            createdFrom = dateFrom,
            createdTo = dateTo,
            classId = initialFilter.classId // сохраняем classId из начального фильтра
        )
    }
}