package stud.euktop.data.mock

import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.RoleSchools
import java.util.Date

object MockUserDataSource {
    private val _users = mutableListOf<UserInfo>().apply {
        add(
            UserInfo(
                userId = 1,
                lastName = "Иванов",
                firstName = "Иван",
                surName = "Иванович",
                birthday = null,
                gender = Gender.MALE,
                email = "noLox@school.ru",
                phone = "+71212121219",
                roles = listOf(
                    RoleSchools(Role.ADMIN, null),
                    RoleSchools(Role.STUDENT, MockSchoolDataSource.getAll()[3]),
                    RoleSchools(Role.STUDENT, MockSchoolDataSource.getAll()[2]),
                ),
                dateRegistration = Date(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserInfo(
                userId = 2,
                lastName = "Петрова",
                firstName = "Анна",
                surName = "Сергеевна",
                birthday = null,
                gender = Gender.WOMAN,
                email = "petrova@school.ru",
                phone = null,
                roles = listOf(
                    RoleSchools(Role.TEACHER, MockSchoolDataSource.getAll()[1]),
                ),
                dateRegistration = Date(),
                accountStatus = AccountStatus.PENDING
            )
        )
        add(
            UserInfo(
                userId = 3,
                lastName = "Сидоров",
                firstName = "Алексей",
                surName = null,
                birthday = null,
                gender = Gender.MALE,
                email = "sidorov@school.ru",
                phone = null,
                roles = listOf(
                    RoleSchools(Role.DIRECTOR, MockSchoolDataSource.getAll()[2]),
                ),
                dateRegistration = Date(),
                accountStatus = AccountStatus.BLOCKED
            )
        )
        add(
            UserInfo(
                userId = 4,
                lastName = "Борисова",
                firstName = "Вера",
                surName = "Владимировна",
                birthday = null,
                gender = Gender.WOMAN,
                email = "borisova@school.ru",
                phone = null,
                roles = emptyList(),
                dateRegistration = Date(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserInfo(
                userId = 5,
                lastName = "Дмитриева",
                firstName = "Елена",
                surName = null,
                birthday = null,
                gender = Gender.WOMAN,
                email = "dmitrieva@school.ru",
                phone = null,
                roles = emptyList(),
                dateRegistration = Date(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
    }

    private val _userRoles = mutableListOf<Triple<Int, Role, School?>>().apply {
        add(Triple(1, Role.ADMIN, null))
        add(Triple(2, Role.TEACHER, MockSchoolDataSource.getAll()[1]))
        add(Triple(3, Role.STUDENT, MockSchoolDataSource.getAll()[1]))
        add(Triple(4, Role.STUDENT, MockSchoolDataSource.getAll()[1]))
        add(Triple(5, Role.STUDENT, MockSchoolDataSource.getAll()[1]))
    }

    val currentUser = UserInfo(
        userId = 1,
        lastName = "Иванов",
        firstName = "Иван",
        surName = "Иванович",
        birthday = null,
        gender = Gender.MALE,
        email = "ivanov@school.ru",
        phone = "+71234567890",
        roles = listOf(
            RoleSchools(Role.ADMIN, null),
            RoleSchools(Role.TEACHER, MockSchoolDataSource.getAll()[1])
        ),
        dateRegistration = Date(),
        accountStatus = AccountStatus.ACTIVE
    )

    fun getAllUsersWithRoles(): List<UserInfo> {
        return _users.map { user ->
            val roles = _userRoles.filter { it.first == user.userId }
                .map { RoleSchools(it.second, it.third) }
            user.copy(roles = roles)
        }
    }

    fun getUser(userId: Int): UserInfo? {
        val user = _users.find { it.userId == userId } ?: return null
        return user
    }

    fun addUser(user: UserInfo, roles: List<RoleSchools>): UserInfo {
        val newId = (_users.maxOfOrNull { it.userId } ?: 0) + 1
        val newUser = user.copy(userId = newId, dateRegistration = Date())
        _users.add(newUser)
        roles.forEach { roleSchool ->
            _userRoles.add(Triple(newId, roleSchool.role, roleSchool.school))
        }
        return newUser
    }

    fun updateUser(user: UserInfo) {
        val index = _users.indexOfFirst { it.userId == user.userId }
        if (index >= 0) {
            _users[index] = user
            _userRoles.removeAll { it.first == user.userId }
            user.roles.forEach { roleSchool ->
                _userRoles.add(Triple(user.userId, roleSchool.role, roleSchool.school))
            }
        }
    }

    fun deleteUser(userId: Int): Boolean {
        val removed = _users.removeIf { it.userId == userId }
        if (removed) _userRoles.removeAll { it.first == userId }
        return removed
    }

    fun getTeachersByRole(role: Role): List<UserInfo> {
        val teacherIds = _userRoles.filter { it.second == role }.map { it.first }.distinct()
        return teacherIds.mapNotNull { userId -> getUser(userId) }
    }

    fun getAllRoles(): List<Triple<Int, Role, School?>> = _userRoles.toList()
}