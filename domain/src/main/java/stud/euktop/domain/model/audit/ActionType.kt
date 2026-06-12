package stud.euktop.domain.model.audit

enum class ActionType {
    CREATE, UPDATE, DELETE, LOGIN;

    companion object {
        fun fromString(value: String): ActionType? =
            entries.find { it.name.equals(value, ignoreCase = true) }
    }
}