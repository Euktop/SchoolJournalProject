package stud.euktop.schooljournal.presentation.auth.common.impl

import stud.euktop.domain.model.Gender
import stud.euktop.domain.model.Profile
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

    private var pendingProfile: Profile? = null

    override suspend fun login(email: String, password: String): CoordinatorResult<Profile> {
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
    ) {
        pendingProfile = Profile(
            userId = 0,
            lastName = lastName,
            firstName = firstName,
            surName = surName,
            gender = gender,
            birthDay = birthDay,
            email = email,
            phone = phone,
            dateRegistration = Date(),
            accountStatusId = stud.euktop.domain.model.AccountStatus.ACTIVE,
            roles = emptyList()
        )
    }

    override suspend fun register(password: String): CoordinatorResult<Profile> {
        val profile = pendingProfile
            ?: return CoordinatorResult.Error(
                NavCommand.Back,
                stud.euktop.schooljournal.R.string.error_empty_body
            )
        return coordinatorExec.exec { authRepository.registration(profile, password) }
    }

    override suspend fun logout(): CoordinatorResult<Unit> {
        pendingProfile = null
        return CoordinatorResult.Success(Unit)
    }

    override suspend fun getUser(): CoordinatorResult<Profile> {
        return coordinatorExec.exec { authRepository.getCurrentUser() }
    }
}