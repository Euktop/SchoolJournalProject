package stud.euktop.schooljournal.presentation.common.filter.school

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.uikit.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.input.SchJInput

@AndroidEntryPoint
class SchoolFilterDialog(
    initialFilter: SchoolFilter,
    onFilterApplied: (SchoolFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<SchoolFilterViewModel, SchoolFilter>(
    initialFilter, onFilterApplied, onError
) {
    companion object{
        const val TAG = "SchoolFilterDialog"
    }

    override val viewModel: SchoolFilterViewModel by viewModels()
    private lateinit var inputs: List<SchJInput>

    override val setups: List<suspend () -> Unit> = emptyList()

    override fun setupFilterFields(container: LinearLayout) {
        inputs = Utils.inputInit(
            container,
            R.string.school_name to initialFilter.name,
            R.string.region to initialFilter.region,
            R.string.address to initialFilter.address,
        )
    }

    override fun resetFilters() {
        Utils.inputClears(inputs)
    }

    override fun collectFilter(): SchoolFilter {
        Utils.inputText(inputs).apply {
            return SchoolFilter(
                name = removeFirstOrNull(),
                region = removeFirstOrNull(),
                address = removeFirstOrNull()
            )
        }
    }
}