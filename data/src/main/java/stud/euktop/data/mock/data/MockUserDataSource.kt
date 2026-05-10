package stud.euktop.data.mock.data

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import java.util.Date

internal object MockUserDataSource {
    private val _users = mutableListOf<UserProfile>().apply {
        add(
            UserProfile(
                userId = 1,
                lastName = "Иванов",
                firstName = "Иван",
                surName = "Иванович",
                birthday = null,
                gender = Gender.MALE,
                email = "noLox@school.ru",
                phone = "+71212121219",
                roles = listOf(
                    createRole(Role.ADMIN, null),
                    createRole(Role.STUDENT, MockSchoolDataSource.getAll()[3]),
                    createRole(Role.STUDENT, MockSchoolDataSource.getAll()[2]),
                ),
                dateRegistration = Date(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserProfile(
                userId = 2,
                lastName = "Петрова",
                firstName = "Анна",
                surName = "Сергеевна",
                birthday = null,
                gender = Gender.FEMALE,
                email = "petrova@school.ru",
                phone = null,
                roles = listOf(
                    createRole(Role.TEACHER, MockSchoolDataSource.getAll()[1]),
                ),
                dateRegistration = Date(),
                accountStatus = AccountStatus.PENDING
            )
        )
        add(
            UserProfile(
                userId = 3,
                lastName = "Сидоров",
                firstName = "Алексей",
                surName = null,
                birthday = null,
                gender = Gender.MALE,
                email = "sidorov@school.ru",
                phone = null,
                roles = listOf(
                    UserRole(Role.DIRECTOR, MockSchoolDataSource.getAll()[2].schoolId, Date()),
                ),
                dateRegistration = Date(),
                accountStatus = AccountStatus.BLOCKED
            )
        )
        add(
            UserProfile(
                userId = 4,
                lastName = "Борисова",
                firstName = "Вера",
                surName = "Владимировна",
                birthday = null,
                gender = Gender.FEMALE,
                email = "borisova@school.ru",
                phone = null,
                roles = emptyList(),
                dateRegistration = Date(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserProfile(
                userId = 5,
                lastName = "Дмитриева",
                firstName = "Елена",
                surName = null,
                birthday = null,
                gender = Gender.FEMALE,
                email = "dmitrieva@school.ru",
                phone = null,
                roles = emptyList(),
                dateRegistration = Date(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
    }

    private fun createRole(role: Role, school: School?) = UserRole(
        role = role,
        schoolId = school?.schoolId,
        assignedAt = Date()
    )

    private val _userRoles = mutableListOf<Triple<Int, Role, Int?>>().apply {
        add(Triple(1, Role.ADMIN, null))
        add(Triple(2, Role.TEACHER, 1))
        add(Triple(3, Role.STUDENT, 1))
        add(Triple(4, Role.STUDENT, 1))
        add(Triple(5, Role.STUDENT, 1))
    }

    val currentUser = UserProfile(
        userId = 1,
        lastName = "Иванов",
        firstName = "Иван",
        surName = "Иванович",
        birthday = null,
        gender = Gender.MALE,
        email = "ivanov@school.ru",
        phone = "+71234567890",
        dateRegistration = Date(),
        accountStatus = AccountStatus.ACTIVE,
        roles = listOf(
            UserRole(Role.ADMIN, null, Date()),
            UserRole(Role.TEACHER, 1, Date())
        )
    )

    fun addUser(user: UserProfile, roles: List<UserRole>): UserProfile {
        val newId = (_users.maxOfOrNull { it.userId } ?: 0) + 1
        val newUser = user.copy(userId = newId, dateRegistration = Date())
        _users.add(newUser)
        roles.forEach { role ->
            _userRoles.add(Triple(newId, role.role, role.schoolId))
        }
        return newUser
    }

    fun updateUser(user: UserProfile) {
        val index = _users.indexOfFirst { it.userId == user.userId }
        if (index >= 0) {
            _users[index] = user
            _userRoles.removeAll { it.first == user.userId }
            user.roles.forEach { role ->
                _userRoles.add(Triple(user.userId, role.role, role.schoolId))
            }
        }
    }

    fun deleteUser(userId: Int): Boolean {
        val removed = _users.removeIf { it.userId == userId }
        if (removed) _userRoles.removeAll { it.first == userId }
        return removed
    }

    fun getTeachersByRole(role: Role): List<UserProfile> {
        val teacherIds = _userRoles.filter { it.second == role }.map { it.first }.distinct()
        return teacherIds.mapNotNull { userId -> getUser(userId) }
    }

    fun getAllRoles(): List<Triple<Int, Role, Int?>> = _userRoles.toList()

    private fun enrichWithRoles(user: UserProfile): UserProfile {
        val roles = _userRoles.filter { it.first == user.userId }.map {
            UserRole(it.second, it.third, Date())
        }
        return user.copy(roles = roles)
    }

    fun getUser(userId: Int): UserProfile? {
        val user = _users.find { it.userId == userId } ?: return null
        return enrichWithRoles(user)
    }

    fun getAllUsersWithRoles(): List<UserProfile> {
        return _users.map { enrichWithRoles(it) }
    }

}

internal fun UserProfile.fullName(): String = buildString {
    append(lastName)
    append(" ")
    append(firstName)
    surName?.let { append(" ").append(it) }
}
