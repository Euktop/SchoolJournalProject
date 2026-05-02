package stud.euktop.data.mock

internal inline fun <T> checkNull(value: T?, action: (T) -> Boolean) =
    value == null || action(value)

internal fun <T> filterParam(vararg filters: Pair<T?, T>) =
    filters.all { v -> checkNull(v.first) { it == v.second } }

internal fun filterStringParam(vararg filters: Pair<String?, String>) =
    filters.all { f ->
        checkNull(f.first) { f.second.contains(it) }
    }
