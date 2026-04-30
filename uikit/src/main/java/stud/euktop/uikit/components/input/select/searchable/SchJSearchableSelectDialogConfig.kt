package stud.euktop.uikit.components.input.select.searchable

import stud.euktop.uikit.components.input.select.ListSafe

data class SchJSearchableSelectDialogConfig<T, C>(
    val title: String = "",
    val items: ListSafe<T> = ListSafe(),
    val categories: ListSafe<C> = ListSafe(),
    val onSearchQueryChanged: (String) -> Unit = {}
)