package stud.euktop.data.mock.util

internal inline fun <T> checkNull(value: T?, action: (T) -> Boolean) =
    value == null || action(value)

internal fun <T> filterParam(vararg filters: Pair<T?, T>) =
    filters.all { filter -> checkNull(filter.first) { it == filter.second } }

internal fun filterStringParam(vararg filters: Pair<String?, String>) =
    filters.all { filter ->
        checkNull(filter.first) { ch ->
            filter.second.contains(
                ch,
                ignoreCase = true
            )
        }
    }
