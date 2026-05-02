package stud.euktop.uikit.components.input.select.searchable

import stud.euktop.uikit.components.input.select.ListSafe

data class SchJSearchableSelectDialogConfig<T>(
    val title: String = "",
    val items: ListSafe<T> = ListSafe(),
    val showFilterDialog: (() -> Unit)? = null
)