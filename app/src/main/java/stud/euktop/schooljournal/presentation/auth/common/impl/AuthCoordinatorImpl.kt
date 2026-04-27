package stud.euktop.schooljournal.presentation.auth.common.impl

import stud.euktop.domain.model.Gender
import stud.euktop.domain.model.Profile
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import java.util.Date
import javax.inject.Inject

class AuthCoordinatorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val coordinatorExec: CoordinatorExec
) : AuthCoordinator {
    private var pendingProfile: Profile? = null

    override suspend fun login(
        email: String,
        password: String
    ): CoordinatorResult<Profile> {
        return coordinatorExec.exec {
            authRepository.login(email, password).onSuccess {
                pendingProfile = it
            }
        }
    }

    override suspend fun saveProfile(
        lastName: String,
        firstName: String,
        surName: String,
        gender: Gender,
        birthDay: Date,
        email: String,
        phone: String?,
    ) {
        pendingProfile = Profile(
            lastName = lastName,
            firstName = firstName,
            surName = surName,
            gender = gender,
            birthDay = birthDay,
            email = email,
            phone = phone,
        )
    }

    override suspend fun register(password: String): CoordinatorResult<Profile> {

    }

    override suspend fun logout(): CoordinatorResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): CoordinatorResult<Profile> {
        TODO("Not yet implemented")
    }
}