package stud.euktop.schooljournal.presentation.auth.common.impl

import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuth
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthCoordinatorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val routerAuth: RouterAuth,
    private val coordinatorExec: CoordinatorExec,
) : AuthCoordinator {

    private var pendingUserProfile: UserProfile? = null

    override suspend fun login(email: String, password: String): CoordinatorResult<Unit> {
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
            pendingUserProfile = UserProfile(
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

    override suspend fun register(password: String): CoordinatorResult<Unit> {
        val profile = pendingUserProfile
            ?: return CoordinatorResult.Error(
                navAction = { routerAuth.toSuccessCreate() },
                stud.euktop.schooljournal.R.string.error_empty_body
            )
        return coordinatorExec.exec { authRepository.registration(profile, password) }
    }

    override suspend fun logout(): CoordinatorResult<Unit> {
        pendingUserProfile = null
        return CoordinatorResult.Success(Unit)
    }

    override suspend fun getUser(): CoordinatorResult<UserProfile> {
        return coordinatorExec.exec { authRepository.getCurrentUser() }
    }

    override suspend fun getRoles(): CoordinatorResult<List<Role>> {
        return coordinatorExec.exec { authRepository.getRoles() }
    }

    override suspend fun saveRole(role: Role): CoordinatorResult<Unit> {
        return coordinatorExec.exec { authRepository.saveRole(role) }
    }
}