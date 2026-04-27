package stud.euktop.data.repository

import stud.euktop.data.map.toDataError
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.network.api.SchoolJournalClientApi
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: SchoolJournalClientApi
) : AuthRepository {

    override suspend fun login(
        login: String,
        passwordHash: String
    ): Result<Unit> {
        return api.login(login, passwordHash).fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it.toDataError())
            }
        )
    }
}