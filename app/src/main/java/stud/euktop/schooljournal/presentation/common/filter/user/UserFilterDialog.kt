package stud.euktop.schooljournal.presentation.common.filter.user

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserInfoFilter
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.base.BaseFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.school.SchoolFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import stud.euktop.uikit.components.filter.FilterFieldBuilder
import stud.euktop.uikit.components.input.SchJInput
import stud.euktop.uikit.components.input.select.ListSafe

@AndroidEntryPoint
class UserFilterDialog(
    initialFilter: UserInfoFilter,
    onFilterApplied: (UserInfoFilter) -> Unit,
    onError: (CoordinatorResult.Error) -> Unit
) : BaseFilterDialog<UserFilterViewModel, UserInfoFilter>(
    initialFilter, onFilterApplied, onError
) {

    override val viewModel: UserFilterViewModel by viewModels()

    private lateinit var fullNameInput: SchJInput
    private var roleSelect: (FilterFieldBuilder.SelectResult<Role>)? = null
    private var statusSelect: (FilterFieldBuilder.SelectResult<AccountStatus>)? = null
    private lateinit var schoolSelect: FilterFieldBuilder.AddSearchableSelectResult<School>

    override val setups: List<suspend () -> Unit> =
        listOf({ setupRoles() }, { setupSchools() }, { setupStatuses() })

    private suspend fun setupRoles() {
        viewModel.roles.collect { roles ->
            roleSelect?.register?.updateItems(roles)
        }
    }

    private suspend fun setupSchools() {
        viewModel.schools.collect { schools ->
            schoolSelect.register.updateItems(schools)
        }
    }

    private suspend fun setupStatuses() {
        viewModel.statuses.collect { statuses ->
            statusSelect?.register?.updateItems(statuses)
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
            items = ListSafe(
                onClick = { value, _ ->
                    initialFilter = initialFilter.copy(role = value)
                },
                toText = { it?.let { getString(it.toMessageId()) } ?: "" }),
            selectedItem = initialFilter.role,
            onShowing = { viewModel.loadRoles() })
        schoolSelect = FilterFieldBuilder.addSearchableSelect(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.school),
            items = ListSafe(
                toText = { it?.name ?: "" },
                onClick = { it, _ -> initialFilter = initialFilter.copy(school = it) },
            ),
            initialSelectedItem = initialFilter.school,
            showFilterDialog = {
                if (parentFragmentManager.findFragmentByTag(SchoolFilterDialog.TAG) != null) return@addSearchableSelect
                SchoolFilterDialog(
                    initialFilter.schoolFilter,
                    { initialFilter = initialFilter.copy(schoolFilter = it) },
                    onError
                ).show(parentFragmentManager, SchoolFilterDialog.TAG)
            },
            onShowing = { viewModel.loadSchools(initialFilter.schoolFilter) },
        )
        statusSelect = FilterFieldBuilder.addSingleSelect<AccountStatus>(
            parent = container,
            fragmentManager = childFragmentManager,
            title = getString(R.string.status),
            items = ListSafe(
                onClick = { v, _ ->
                    initialFilter = initialFilter.copy(accountStatus = v)
                },
                toText = { it?.let { getString(it.toMessageId()) } ?: "" }),
            onShowing = { viewModel.loadStatuses() })
    }

    override fun resetFilters() {
        fullNameInput.state = fullNameInput.state.copy(text = "")
        roleSelect?.select?.let { it.state = it.state.copy(selectText = "") }
        schoolSelect.select.state = schoolSelect.select.state.copy(selectText = "")
        statusSelect?.select?.let { it.state = it.state.copy(selectText = "") }
        initialFilter = UserInfoFilter()
    }

    override fun collectFilter(): UserInfoFilter {
        return initialFilter.copy(
            fullName = fullNameInput.state.text
        )
    }
    companion object{
        const val TAG = "UserFilterDialog"
    }
}