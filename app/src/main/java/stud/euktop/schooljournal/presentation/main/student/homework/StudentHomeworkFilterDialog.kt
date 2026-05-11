package stud.euktop.schooljournal.presentation.main.student.homework

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.utils.baseDateFormat
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.homework.AppHomeworkFilter
import stud.euktop.schooljournal.presentation.common.filter.subject.SubjectFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import java.util.Date

@AndroidEntryPoint
class StudentHomeworkFilterDialog(
    initialFilter: AppHomeworkFilter,
    onFilterApplied: (AppHomeworkFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<StudentHomeworkFilterViewModel, AppHomeworkFilter>(
    initialFilter, onFilterApplied, onError
) {

    override val viewModel: StudentHomeworkFilterViewModel by viewModels()

    private lateinit var subjectSelectResult: FilterFieldBuilder.AddSearchableSelectResult<Subject>
    private lateinit var dateRangeResult: FilterFieldBuilder.DateRangeResult
    private var selectedSubject: Subject? = initialFilter.subject
    private var dateFrom: Date? = initialFilter.createdFrom
    private var dateTo: Date? = initialFilter.createdTo

    override val setups: List<suspend () -> Unit> = listOf { setupSubjects() }

    private suspend fun setupSubjects() {
        viewModel.subjects.collect { subjects ->
            subjectSelectResult.updateItems(subjects)
        }
    }

    override fun setupFilterFields(container: LinearLayout) {
        subjectSelectResult = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.subject),
            items = emptyList(),
            toText = { it?.name ?: "" },
            onSelected = { subject ->
                selectedSubject = subject
                initialFilter = initialFilter.copy(subject = subject)
            },
            initialSelectedItem = initialFilter.subject,
            onShowing = { viewModel.loadSubjects(initialFilter.subjectFilter ?: SubjectFilter()) },
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SubjectFilterDialog.TAG) != null) return@addSearchableSelect
                val dialog = SubjectFilterDialog(
                    initialFilter = initialFilter.subjectFilter ?: SubjectFilter(),
                    onFilterApplied = { subjectFilter ->
                        initialFilter = initialFilter.copy(subjectFilter = subjectFilter)
                        viewModel.loadSubjects(subjectFilter)
                    },
                    onError = this.onError
                )
                dialog.show(parentFragmentManager, SubjectFilterDialog.TAG)
            }
        )

        dateRangeResult = FilterFieldBuilder.addDateRange(
            parent = container,
            title = getString(R.string.date_create),
            dateFormat = baseDateFormat,
            fromDate = dateFrom,
            toDate = dateTo,
            onFromDateSelected = { date ->
                dateFrom = date
                initialFilter = initialFilter.copy(createdFrom = date)
            },
            onToDateSelected = { date ->
                dateTo = date
                initialFilter = initialFilter.copy(createdTo = date)
            }
        )
    }

    override fun resetFilters() {
        subjectSelectResult.select.state = subjectSelectResult.select.state.copy(selectText = "")
        dateRangeResult.fromInput.state = dateRangeResult.fromInput.state.copy(text = "")
        dateRangeResult.toInput.state = dateRangeResult.toInput.state.copy(text = "")
        selectedSubject = null
        dateFrom = null
        dateTo = null
        initialFilter = AppHomeworkFilter()
    }

    override fun collectFilter(): AppHomeworkFilter {
        return initialFilter.copy(
            subject = selectedSubject,
            createdFrom = dateFrom,
            createdTo = dateTo
        )
    }
}