package stud.euktop.schooljournal.presentation.common.utils

import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.filter.user.AppUserFilter
import stud.euktop.schooljournal.presentation.common.filter.user.UserFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.RepositoryExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.uikit.components.adapter.SchJTextAdapter
import stud.euktop.uikit.components.input.select.content.SelectableAdapter
import stud.euktop.uikit.components.input.select.searchable.SchJSearchableSelect

fun UserListItem.fullName(): String = buildString {
    append(lastName)
    append(" ")
    append(firstName)
    surName?.let { append(" ").append(it) }
}.trim()

object DomainSelectHelper {

    inline fun CoroutineScope.setupUserSelect(
        select: SchJSearchableSelect,
        fragmentManager: FragmentManager,
        repository: UserAdminRepository,
        coordinatorExec: CoordinatorExec,
        crossinline onSelected: (UserListItem?, AppUserFilter) -> Unit,
        onErrorModel: RepositoryExec,
        initialSelectedItem: UserListItem? = null,
        initialFilter: AppUserFilter = AppUserFilter(),
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

                is CoordinatorResult.Success<List<UserListItem>> -> result.result
            } ?: emptyList()
        },
        onSelected = onSelected,
        initialSelectedItem = initialSelectedItem,
        initialFilter = initialFilter,
        onErrorModel = onErrorModel
    )

    inline fun CoroutineScope.setupUserSelect(
        select: SchJSearchableSelect,
        fragmentManager: FragmentManager,
        noinline loadItems: suspend () -> List<UserListItem>,
        crossinline onSelected: (UserListItem?, AppUserFilter) -> Unit,
        initialSelectedItem: UserListItem? = null,
        onErrorModel: RepositoryExec,
        initialFilter: AppUserFilter = AppUserFilter(),
    ) {
        var currentFilter = initialFilter
        setupSearchableSelect(
            select = select,
            fragmentManager = fragmentManager,
            loadItems = loadItems,
            toText = { it?.fullName() ?: "" },
            onSelected = { item -> onSelected(item, currentFilter) },
            showFilterDialog = {
                if (fragmentManager.findFragmentByTag(UserFilterDialog.TAG) != null) return@setupSearchableSelect
                val dialog = UserFilterDialog(
                    currentFilter, { newFilter ->
                        currentFilter = newFilter
                        // можно перезагрузить список, если нужно
                    }, onErrorModel.onError
                )
                dialog.show(fragmentManager, UserFilterDialog.TAG)
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
    ) {
        var adapter: SchJTextAdapter<T>? = null
        select.onShowing = {
            launch {
                val items = loadItems()
                adapter = SchJTextAdapter(object : SchJTextAdapter.Listener<T> {
                    override fun onClick(value: T) {
                        onSelected(value)
                        select.state = select.state.copy(selectText = toText(value))
                    }

                    override fun toText(value: T): String = toText(value)
                    override fun isEquals(value1: T, value2: T): Boolean = value1 == value2
                })
                adapter.submitList(items)
                select.attach(adapter, object : SelectableAdapter<T> {
                    override var onItemSelected: (T?) -> Unit = onSelected
                    override val toText: (value: T?) -> String = toText
                }, fragmentManager, "", showFilterDialog)
            }
        }
        // Устанавливаем начальный текст, если есть
        if (initialSelectedItem != null) {
            select.state = select.state.copy(selectText = toText(initialSelectedItem))
        }
    }
}