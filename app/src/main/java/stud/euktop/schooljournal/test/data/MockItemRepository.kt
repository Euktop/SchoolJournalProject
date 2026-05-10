package stud.euktop.schooljournal.test.data

import kotlinx.coroutines.delay

object MockItemRepository {

    // Всего элементов 100, для демонстрации
    private const val TOTAL_ITEMS = 100

    suspend fun getItems(page: Int, pageSize: Int): List<String> {
        // Имитация задержки сети
        delay(1000)

        val start = page * pageSize
        if (start >= TOTAL_ITEMS) return emptyList()

        val end = minOf(start + pageSize, TOTAL_ITEMS)
        return (start until end).map { "Элемент $it" }
    }

    fun hasMoreItems(page: Int, pageSize: Int): Boolean {
        return (page + 1) * pageSize < TOTAL_ITEMS
    }
}