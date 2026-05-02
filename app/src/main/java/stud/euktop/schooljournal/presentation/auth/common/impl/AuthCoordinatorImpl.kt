package stud.euktop.schooljournal.presentation.auth.common.impl

import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthCoordinatorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val coordinatorExec: CoordinatorExec
) : AuthCoordinator {

    private var pendingUserInfo: UserInfo? = null

    override suspend fun login(email: String, password: String): CoordinatorResult<UserInfo> {
        return coordinatorExec.exec { authRepository.login(email, password) }
    }

    override suspend fun saveProfile(
        lastName: String,
        firstName: String,
        surName: String,
        gender: Gender,
        birthDay: Date,
        email: String,
        phone: String?
    ): Result<Unit> {
        return try {
            pendingUserInfo = UserInfo(
                userId = 0,
                lastName = lastName,
                firstName = firstName,
                surName = surName,
                gender = gender,
                birthday = birthDay,
                email = email,
                phone = phone,
                dateRegistration = Date(),
                accountStatus = AccountStatus.ACTIVE,
                roles = emptyList()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(password: String): CoordinatorResult<UserInfo> {
        val profile = pendingUserInfo
            ?: return CoordinatorResult.Error(
                NavCommand.Back,
                stud.euktop.schooljournal.R.string.error_empty_body
            )
        return coordinatorExec.exec { authRepository.registration(profile, password) }
    }

    override suspend fun logout(): CoordinatorResult<Unit> {
        pendingUserInfo = null
        return CoordinatorResult.Success(Unit)
    }

    override suspend fun getUser(): CoordinatorResult<UserInfo> {
        return coordinatorExec.exec { authRepository.getCurrentUser() }
    }
}