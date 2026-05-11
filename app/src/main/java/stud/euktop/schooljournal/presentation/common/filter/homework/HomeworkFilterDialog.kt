package stud.euktop.schooljournal.presentation.common.filter.homework

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.utils.baseDateFormat
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.subject.SubjectFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput
import java.util.Date

@AndroidEntryPoint
class HomeworkFilterDialog(
    initialFilter: AppHomeworkFilter,
    onFilterApplied: (AppHomeworkFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<HomeworkFilterViewModel, AppHomeworkFilter>(
    initialFilter, onFilterApplied, onError
) {
    companion object {
        const val TAG = "HomeworkFilterDialog"
    }

    override val viewModel: HomeworkFilterViewModel by viewModels()

    private lateinit var descriptionInput: SchJInput
    private lateinit var dateRangeResult: FilterFieldBuilder.DateRangeResult
    private lateinit var subjectSelectResult: FilterFieldBuilder.AddSearchableSelectResult<Subject>
    private var selectedSubject: Subject? = initialFilter.subject
    private var fromDate: Date? = initialFilter.createdFrom
    private var toDate: Date? = initialFilter.createdTo

    override val setups: List<suspend () -> Unit> = listOf { setupSubjects() }

    private suspend fun setupSubjects() {
        viewModel.subjects.collect { subjects ->
            subjectSelectResult.updateItems(subjects)
        }
    }

    override fun setupFilterFields(container: LinearLayout) {
        descriptionInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(stud.euktop.uikit.R.string.search_description),
            initialValue = initialFilter.description ?: ""
        )

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
            onShowing = { viewModel.subjects(initialFilter.subjectFilter ?: SubjectFilter()) },
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SubjectFilterDialog.TAG) != null) return@addSearchableSelect
                val dialog = SubjectFilterDialog(
                    initialFilter = initialFilter.subjectFilter ?: SubjectFilter(),
                    onFilterApplied = { subjectFilter ->
                        initialFilter = initialFilter.copy(subjectFilter = subjectFilter)
                        viewModel.subjects(subjectFilter)
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
            fromDate = fromDate,
            toDate = toDate,
            onFromDateSelected = { date ->
                fromDate = date
                initialFilter = initialFilter.copy(createdFrom = date)
            },
            onToDateSelected = { date ->
                toDate = date
                initialFilter = initialFilter.copy(createdTo = date)
            }
        )
    }

    override fun resetFilters() {
        descriptionInput.state = descriptionInput.state.copy(text = "")
        subjectSelectResult.select.state = subjectSelectResult.select.state.copy(selectText = "")
        dateRangeResult.fromInput.state = dateRangeResult.fromInput.state.copy(text = "")
        dateRangeResult.toInput.state = dateRangeResult.toInput.state.copy(text = "")
        selectedSubject = null
        fromDate = null
        toDate = null
        initialFilter = AppHomeworkFilter()
    }

    override fun collectFilter(): AppHomeworkFilter {
        return initialFilter.copy(
            description = descriptionInput.state.text.takeIf { it.isNotBlank() }
        )
    }
}