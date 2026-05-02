package stud.euktop.schooljournal.presentation.common.filter.homework

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.utils.baseDateFormat
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.subject.SubjectFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput
import stud.euktop.uikit.components.input.select.ListSafe

@AndroidEntryPoint
class HomeworkFilterDialog(
    initialFilter: HomeworkFilter,
    onFilterApplied: (HomeworkFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<HomeworkFilterViewModel, HomeworkFilter>(
    initialFilter, onFilterApplied, onError
) {
    companion object {
        const val TAG = "HomeworkFilterDialog"
    }

    override val viewModel: HomeworkFilterViewModel by viewModels()

    private lateinit var descriptionInput: SchJInput
    private lateinit var dateRangeResult: FilterFieldBuilder.DateRangeResult
    private lateinit var subjectSelectResult: FilterFieldBuilder.AddSearchableSelectResult<Subject>

    override val setups: List<suspend () -> Unit> = listOf { setupSubjects() }

    private suspend fun setupSubjects() {
        viewModel.subjects.collect { subjects ->
            subjectSelectResult.register.updateItems(subjects)
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
            items = ListSafe(
                toText = { it?.name ?: "" },
                onClick = { v, _ -> initialFilter = initialFilter.copy(subject = v) }
            ),
            onShowing = { viewModel.subjects(initialFilter.subjectFilter) },
            initialSelectedItem = initialFilter.subject,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag
                        (SubjectFilterDialog.TAG) != null
                )
                    return@addSearchableSelect
                val dialog = SubjectFilterDialog(
                    initialFilter.subjectFilter,
                    { initialFilter = initialFilter.copy(subjectFilter = it) },
                    this.onError
                )
                dialog.show(parentFragmentManager, SubjectFilterDialog.TAG)
            }
        )

        dateRangeResult = FilterFieldBuilder.addDateRange(
            parent = container,
            title = getString(R.string.date_create),
            dateFormat = baseDateFormat,
            fromDate = initialFilter.createdDiff.dateStart,
            toDate = initialFilter.createdDiff.dateEnd,
            onFromDateSelected = { date ->
                initialFilter =
                    initialFilter.copy(createdDiff = initialFilter.createdDiff.copy(dateStart = date))
            },
            onToDateSelected = { date ->
                initialFilter =
                    initialFilter.copy(createdDiff = initialFilter.createdDiff.copy(dateEnd = date))
            }
        )
    }

    override fun resetFilters() {
        descriptionInput.state = descriptionInput.state.copy(text = "")
        subjectSelectResult.select.state = subjectSelectResult.select.state.copy(selectText = "")
        dateRangeResult.fromInput.state = dateRangeResult.fromInput.state.copy(text = "")
        dateRangeResult.toInput.state = dateRangeResult.toInput.state.copy(text = "")
        initialFilter = HomeworkFilter()
    }

    override fun collectFilter(): HomeworkFilter {
        return initialFilter.copy(
            description = descriptionInput.state.text.takeIf { it.isNotBlank() },
        )
    }
}