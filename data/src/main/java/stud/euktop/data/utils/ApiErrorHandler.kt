package stud.euktop.data.utils

import com.schooljournal.model.ErrorResponse
import org.openapitools.client.infrastructure.ClientError
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerError
import org.openapitools.client.infrastructure.ServerException
import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.utils.loger.logger
import stud.euktop.network.util.ErrorParser
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure


@Singleton
class ApiErrorHandler @Inject constructor() {

    suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: Exception) {
            logger?.e("ExecuteResponse", "ErrorResponse", e)
            when (e) {
                is ServerException -> {
                    val errorResponse = parseErrorResponse(e)
                    failure<T>(mapToDataError(e.statusCode, errorResponse, e.message))
                }
                is IOException -> failure(DataError.NetworkConnection(e.message))
                else -> failure(DataError.Unknown(e.message))
            }
        }
    }

    private fun parseErrorResponse(e: Exception): ErrorResponse? {
        val body = when (e) {
            is ClientException -> (e.response as? ClientError<*>)?.body?.toString()
            is ServerException -> (e.response as? ServerError<*>)?.body?.toString()
            else -> null
        }
        return ErrorParser.parseErrorResponse(body)
    }

    private fun mapToDataError(
        httpCode: Int,
        errorResponse: ErrorResponse?,
        fallbackMessage: String?
    ): DataError {
        val errorCode = errorResponse?.code
        val message = errorResponse?.message ?: fallbackMessage

        return when (httpCode) {
            HttpURLConnection.HTTP_BAD_REQUEST -> when (errorCode) {
                1 -> DataError.SessionContextNotSet(message)
                2 -> DataError.InvalidCredentials(message)
                3 -> DataError.UserNotFound(message)
                4 -> DataError.UserDeleted(message)
                5 -> DataError.UserNotConfirmed(message)
                6 -> DataError.UserBlocked(message)
                7 -> DataError.ClassTimeOverlap(message)
                8 -> DataError.PermissionOutOfRange(message)
                9 -> DataError.MultiplePrimaryTeachers(message)
                10 -> DataError.TeacherCannotTeachSubject(message)
                11 -> DataError.StudentNotInClass(message)
                12 -> DataError.RecordAlreadyFixed(message)
                13 -> DataError.NoPermissionToGrade(message)
                14 -> DataError.FieldRequired(message)
                15 -> DataError.UserNotExist(message)
                16 -> DataError.NoPermissionToManageUser(message)
                203 -> DataError.CannotDeleteClassWithStudents(message)
                204 -> DataError.CannotDeleteClassWithLessons(message)
                207 -> DataError.RoomNotBelongsToSchool(message)
                208 -> DataError.TeacherTimeConflict(message)
                209 -> DataError.ClassTimeConflict(message)
                210 -> DataError.StartTimeAfterEndTime(message)
                211 -> DataError.CannotDeleteSchoolWithClasses(message)
                212 -> DataError.CannotDeleteRoomUsedInLessons(message)
                else -> DataError.HttpError(httpCode, message ?: "Bad request")
            }

            HttpURLConnection.HTTP_NOT_FOUND -> when (errorCode) {
                100 -> DataError.RecordNotFound(message)
                101 -> DataError.SchoolNotFound(message)
                102 -> DataError.UserNotFound(message)
                103 -> DataError.ClassNotFound(message)
                104 -> DataError.SubjectNotFound(message)
                105 -> DataError.TeacherNotFound(message)
                else -> DataError.HttpError(httpCode, message ?: "Not found")
            }

            HttpURLConnection.HTTP_CONFLICT -> when (errorCode) {
                200 -> DataError.UniqueViolation(message)
                201 -> DataError.ForeignKeyViolation(message)
                202 -> DataError.RecordAlreadyExists(message)
                else -> DataError.HttpError(httpCode, message ?: "Conflict")
            }

            HttpURLConnection.HTTP_FORBIDDEN -> DataError.AccessDenied(message ?: "Forbidden")
            HttpURLConnection.HTTP_INTERNAL_ERROR -> DataError.InternalServerError(
                message ?: "Internal server error"
            )

            else -> DataError.HttpError(httpCode, message ?: "Unknown error")
        }
    }
}