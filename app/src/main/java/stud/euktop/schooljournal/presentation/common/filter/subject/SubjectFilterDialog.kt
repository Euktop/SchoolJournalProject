package stud.euktop.schooljournal.presentation.common.filter.subject

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput

@AndroidEntryPoint
class SubjectFilterDialog(
    initialFilter: SubjectFilter,
    onFilterApplied: (SubjectFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<SubjectFilterViewModel, SubjectFilter>(
    initialFilter, onFilterApplied, onError
) {
    companion object {
        const val TAG = "SubjectFilterDialog"
    }

    override val viewModel: SubjectFilterViewModel by viewModels()

    private lateinit var descriptionInput: SchJInput

    override val setups: List<suspend () -> Unit> = emptyList()

    override fun setupFilterFields(container: LinearLayout) {
        descriptionInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(stud.euktop.uikit.R.string.search_description),
            initialValue = initialFilter.name ?: ""
        )
    }

    override fun resetFilters() {
        descriptionInput.state = descriptionInput.state.copy(text = "")
        initialFilter = SubjectFilter()
    }

    override fun collectFilter(): SubjectFilter {
        return SubjectFilter(
            name = descriptionInput.state.text.takeIf { it.isNotBlank() }
        )
    }
}