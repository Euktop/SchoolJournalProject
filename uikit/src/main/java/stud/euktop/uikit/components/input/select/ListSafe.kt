package stud.euktop.uikit.components.input.select

data class ListSafe<V>(
    val values: List<V> = emptyList(),
    val toText: (V) -> String = { it.toString() },
    val onClick: (V, Boolean) -> Unit = { _, _ -> }
)