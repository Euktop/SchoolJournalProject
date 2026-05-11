package stud.euktop.schooljournal.presentation.common.base

/**
 * Базовое состояние для всех экранов.
 * @param T тип конкретного состояния (для ковариантного copy)
 */
abstract class BaseState<T : BaseState<T>> {
    /** Карта загрузок: ключ операции -> true/false */
    abstract val loadingMap: Map<String, Boolean>

    /**
     * Проверить, идёт ли загрузка по конкретному ключу.
     */
    fun isLoading(key: String): Boolean = loadingMap[key] == true

    /**
     * Есть ли хотя бы одна активная загрузка.
     */
    fun isAnyLoading(): Boolean = loadingMap.values.any { it }

    /**
     * Создать новое состояние с обновлённым значением загрузки для указанного ключа.
     */
    fun withLoading(key: String, isLoading: Boolean): T {
        val map = loadingMap.toMutableMap()
        map[key] = isLoading
        return updateIsLoading(map)
    }

    abstract fun updateIsLoading(loadingMap: Map<String, Boolean>): T

}