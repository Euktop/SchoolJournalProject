package stud.euktop.schooljournal.presentation.common.filter.user

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Role
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput

@AndroidEntryPoint
class UserFilterDialog(
    initialFilter: AppUserFilter,
    onFilterApplied: (AppUserFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<UserFilterViewModel, AppUserFilter>(
    initialFilter, onFilterApplied, onError
) {

    override val viewModel: UserFilterViewModel by viewModels()

    private lateinit var fullNameInput: SchJInput
    private var roleSelect: FilterFieldBuilder.SelectResult<Role>? = null
    private lateinit var schoolSelect: FilterFieldBuilder.AddSearchableSelectResult<School>
    private var statusSelect: FilterFieldBuilder.SelectResult<AccountStatus>? = null

    override val setups: List<suspend () -> Unit> = listOf(
        { setupRoles() },
        { setupSchools() },
        { setupStatuses() }
    )

    private suspend fun setupRoles() {
        viewModel.roles.collect { roles ->
            roleSelect?.updateItems(roles)
        }
    }

    private suspend fun setupSchools() {
        viewModel.schools.collect { schools ->
            schoolSelect.updateItems(schools)
        }
    }

    private suspend fun setupStatuses() {
        viewModel.statuses.collect { statuses ->
            statusSelect?.updateItems(statuses)
        }
    }

    override fun setupFilterFields(container: LinearLayout) {
        fullNameInput = FilterFieldBuilder.addText(
            parent = container,
            title = getString(R.string.name),
            initialValue = initialFilter.fullName ?: ""
        )

        roleSelect = FilterFieldBuilder.addSingleSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.role),
            items = emptyList(),
            toText = { it?.let { getString(it.toMessageId()) } ?: "" },
            onSelected = { role -> initialFilter = initialFilter.copy(role = role) },
            selectedItem = initialFilter.role,
            onShowing = { viewModel.loadRoles() }
        )

        schoolSelect = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.school),
            items = emptyList(),
            toText = { it?.name ?: "" },
            onSelected = { school -> initialFilter = initialFilter.copy(school = school) },
            initialSelectedItem = initialFilter.school,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SchoolFilterDialog.TAG) != null) return@addSearchableSelect
                SchoolFilterDialog(
                    initialFilter = initialFilter.schoolFilter ?: SchoolFilter(),
                    onFilterApplied = { schoolFilter ->
                        initialFilter = initialFilter.copy(schoolFilter = schoolFilter)
                        viewModel.loadSchools(schoolFilter)
                    },
                    onError = this.onError
                ).show(parentFragmentManager, SchoolFilterDialog.TAG)
            },
            onShowing = { viewModel.loadSchools(initialFilter.schoolFilter ?: SchoolFilter()) }
        )

        statusSelect = FilterFieldBuilder.addSingleSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.status),
            items = emptyList(),
            toText = { it?.let { getString(it.toMessageId()) } ?: "" },
            onSelected = { status -> initialFilter = initialFilter.copy(accountStatus = status) },
            selectedItem = initialFilter.accountStatus,
            onShowing = { viewModel.loadStatuses() }
        )
    }

    override fun resetFilters() {
        fullNameInput.state = fullNameInput.state.copy(text = "")
        roleSelect?.select?.let { it.state = it.state.copy(selectText = "") }
        schoolSelect.select.state = schoolSelect.select.state.copy(selectText = "")
        statusSelect?.select?.let { it.state = it.state.copy(selectText = "") }
        initialFilter = AppUserFilter()
    }

    override fun collectFilter(): AppUserFilter {
        return initialFilter.copy(
            fullName = fullNameInput.state.text.takeIf { it.isNotBlank() }
        )
    }

    companion object {
        const val TAG = "UserFilterDialog"
    }
}