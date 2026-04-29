package stud.euktop.schooljournal.presentation.common.message.contract

data class MessageParam(
    val message: Int,
    val action: (() -> Unit)? = null,
    val dismiss: (() -> Unit)? = null
)