package stud.euktop.schooljournal.presentation.common.filter.room

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.RoomFilter
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput

@AndroidEntryPoint
class RoomFilterDialog(
    initialFilter: RoomFilter,
    onFilterApplied: (RoomFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<RoomFilterViewModel, RoomFilter>(
    initialFilter, onFilterApplied, onError
) {

    override val viewModel: RoomFilterViewModel by viewModels()
    override val setups: List<suspend () -> Unit> = emptyList()

    private lateinit var nameInput: SchJInput

    override fun setupFilterFields(container: LinearLayout) {
        nameInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(stud.euktop.uikit.R.string.room),
            initialValue = initialFilter.name ?: ""
        )
    }

    override fun resetFilters() {
        nameInput.state = nameInput.state.copy(text = "")
        initialFilter = RoomFilter()
    }

    override fun collectFilter(): RoomFilter {
        return RoomFilter(
            name = nameInput.state.text.takeIf { it.isNotBlank() }
        )
    }
}