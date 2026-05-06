package stud.euktop.schooljournal.presentation.common.utils

import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.user.UserInfoFilter
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.filter.subject.SubjectFilterDialog
import stud.euktop.schooljournal.presentation.common.filter.user.UserFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.RepositoryExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect

object DomainSelectHelper {

    inline fun CoroutineScope.setupUserSelect(
        select: SchJSearchableSelect,
        fragmentManager: FragmentManager,
        repository: UserAdminRepository,
        coordinatorExec: CoordinatorExec,
        crossinline onSelected: (UserInfo?, UserInfoFilter) -> Unit,
        onErrorModel: RepositoryExec,
        initialSelectedItem: UserInfo? = null,
        initialFilter: UserInfoFilter = UserInfoFilter(),
    ) = setupUserSelect(
        select = select,
        fragmentManager = fragmentManager,
        loadItems = {
            val result = coordinatorExec.exec {
                repository.getUsers()
            }
            when (result) {
                is CoordinatorResult.Error -> {
                    onErrorModel.onError(result)
                    null
                }

                is CoordinatorResult.Success<List<UserInfo>> -> {
                    result.result
                }
            }
        },
        onSelected = onSelected,
        initialSelectedItem = initialSelectedItem,
        initialFilter = initialFilter,
        onErrorModel = onErrorModel
    )

    inline fun CoroutineScope.setupUserSelect(
        select: SchJSearchableSelect,
        fragmentManager: FragmentManager,
        crossinline loadItems: suspend () -> List<UserInfo>?,
        crossinline onSelected: (UserInfo?, UserInfoFilter) -> Unit,
        initialSelectedItem: UserInfo? = null,
        onErrorModel: RepositoryExec,
        initialFilter: UserInfoFilter = UserInfoFilter(),
    ) {
        var initialFilter = initialFilter
        var list: List<UserInfo> = emptyList()
        setupSearchableSelect(
            select = select,
            fragmentManager = fragmentManager,
            loadItems = {
                val result = loadItems()
                if (result != null) list = result
                list
            },
            toText = { it?.fullName ?: "" },
            onSelected = { onSelected(it, initialFilter) },
            showFilterDialog = {
                if (fragmentManager.findFragmentByTag(UserFilterDialog.TAG) != null) return@setupSearchableSelect
                val dialog = UserFilterDialog(
                    initialFilter, { initialFilter = it }, onErrorModel.onError
                )
                dialog.show(fragmentManager, SubjectFilterDialog.TAG)
            },
            initialSelectedItem = initialSelectedItem
        )
    }


    fun <T : Any> CoroutineScope.setupSearchableSelect(
        select: SchJSearchableSelect,
        fragmentManager: FragmentManager,
        loadItems: suspend () -> List<T>,
        toText: (T?) -> String,
        onSelected: (T?) -> Unit,
        showFilterDialog: (() -> Unit)? = null,
        initialSelectedItem: T? = null
    ): SchJSearchableSelect.RegisterList<T> {
        var register: SchJSearchableSelect.RegisterList<T>? = null
        val itemsSafe = ListSafe<T>(
            toText = toText, onClick = { item, _ -> onSelected(item) })
        register = addSearchableSelect(
            select = select,
            fragmentManager = fragmentManager,
            items = itemsSafe,
            initialSelectedItem = initialSelectedItem,
            showFilterDialog = showFilterDialog,
            onShowing = {
                launch {
                    register?.updateItems(loadItems())
                }
            })
        return register
    }

    fun <T : Any> addSearchableSelect(
        select: SchJSearchableSelect,
        fragmentManager: FragmentManager,
        items: ListSafe<T> = ListSafe(),
        initialSelectedItem: T? = null,
        showFilterDialog: (() -> Unit)? = null,
        onShowing: (() -> Unit)? = null,
    ): SchJSearchableSelect.RegisterList<T> {
        fun updateText(v: T) {
            select.state = select.state.copy(selectText = items.toText(v))
        }

        val register = select.RegisterList(items, showFilterDialog).apply {
            register(fragmentManager)
        }
        select.onShowing = onShowing

        if (initialSelectedItem != null) {
            updateText(initialSelectedItem)
        }
        return register
    }
}