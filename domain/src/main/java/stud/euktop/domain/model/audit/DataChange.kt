package stud.euktop.domain.model.audit

data class DataChange(
    val oldValue: Map<String, Any?>,
    val newValue: Map<String, Any?>
)