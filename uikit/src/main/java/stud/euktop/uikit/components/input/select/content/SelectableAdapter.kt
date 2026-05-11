package stud.euktop.uikit.components.input.select.content

interface SelectableAdapter<T> {
    var onItemSelected: (T?) -> Unit
    val toText: (value: T?) -> String
}