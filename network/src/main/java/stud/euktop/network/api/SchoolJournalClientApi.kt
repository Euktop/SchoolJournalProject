package stud.euktop.network.api

import com.schooljournal.model.ErrorResponse
import com.schooljournal.model.LoginRequest
import com.schooljournal.model.LoginResponse
import org.openapitools.client.infrastructure.ClientError
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.Serializer.moshi
import org.openapitools.client.infrastructure.ServerException
import stud.euktop.network.NetworkClient
import stud.euktop.network.util.NetworkError
import java.io.IOException
import javax.inject.Inject

class SchoolJournalClientApi @Inject constructor(
    private val networkClient: NetworkClient
) {
    private val authApi = networkClient.authorizationApi()
    private val testApi = networkClient.testApi()

    private suspend inline fun <reified T> result(
        noinline block: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(block())
        } catch (e: ClientException) {
            val errorBody = e.response?.let { (it as? ClientError<*>)?.body?.toString() }
            val error = errorBody?.let { moshi.adapter(ErrorResponse::class.java).fromJson(it) }
            Result.failure(NetworkError.HttpError(e.statusCode, error?.message ?: e.message))
        } catch (e: ServerException) {
            Result.failure(NetworkError.HttpError(e.statusCode, e.message))
        } catch (e: IOException) {
            Result.failure(NetworkError.NetworkConnection(e.message))
        }
    }

    suspend fun login(login: String, passwordHash: String): Result<LoginResponse> =
        result { authApi.apiAuthorizationLoginPost(LoginRequest(login, passwordHash)) }

    suspend fun testMe(): Result<Unit> =
        result { testApi.apiTestMeGet() }
}