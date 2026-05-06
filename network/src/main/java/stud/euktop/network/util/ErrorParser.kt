package stud.euktop.network.util

import com.schooljournal.model.ErrorResponse
import org.openapitools.client.infrastructure.Serializer

object ErrorParser {
    /**
     * Парсит тело ошибки (строку JSON) в [ErrorResponse].
     * @param json строковое представление JSON (может быть null)
     * @return [ErrorResponse] или null, если парсинг не удался
     */
    fun parseErrorResponse(json: String?): ErrorResponse? {
        return json?.let {
            try {
                Serializer.moshi.adapter(ErrorResponse::class.java).fromJson(it)
            } catch (e: Exception) {
                null
            }
        }
    }
}