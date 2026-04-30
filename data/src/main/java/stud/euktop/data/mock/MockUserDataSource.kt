package stud.euktop.data.mock

import stud.euktop.domain.model.auth.Profile
import stud.euktop.domain.model.auth.Role
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.AccountStatus
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.model.user.RoleSchools
import stud.euktop.domain.model.user.UserInfo
import java.util.Date

object MockUserDataSource {
    // Хранилище пользователей
    private val _users = mutableListOf<UserInfo>().apply {
        add(
            UserInfo(
                userId = 1,
                lastName = "Иванов",
                firstName = "Иван",
                surName = "Иванович",
                email = "ivanov@school.ru",
                phone = "+71234567890",
                roles = emptyList(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserInfo(
                userId = 2,
                lastName = "Петрова",
                firstName = "Анна",
                surName = "Сергеевна",
                email = "petrova@school.ru",
                phone = null,
                roles = emptyList(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserInfo(
                userId = 3,
                lastName = "Сидоров",
                firstName = "Алексей",
                surName = null,
                email = "sidorov@school.ru",
                phone = null,
                roles = emptyList(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserInfo(
                userId = 4,
                lastName = "Борисова",
                firstName = "Вера",
                surName = "Владимировна",
                email = "borisova@school.ru",
                phone = null,
                roles = emptyList(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
        add(
            UserInfo(
                userId = 5,
                lastName = "Дмитриева",
                firstName = "Елена",
                surName = null,
                email = "dmitrieva@school.ru",
                phone = null,
                roles = emptyList(),
                accountStatus = AccountStatus.ACTIVE
            )
        )
    }

    // Хранилище ролей (userId, role, school)
    private val _userRoles = mutableListOf<Triple<Int, Role, School?>>().apply {
        add(Triple(1, Role.ADMIN, null))
        add(Triple(2, Role.TEACHER, MockSchoolDataSource.getAll()[1]))
        add(Triple(3, Role.STUDENT, MockSchoolDataSource.getAll()[1]))
        add(Triple(4, Role.STUDENT, MockSchoolDataSource.getAll()[1]))
        add(Triple(5, Role.STUDENT, MockSchoolDataSource.getAll()[1]))
    }

    // Текущий профиль (для авторизации)
    val currentUser = Profile(
        userId = 1,
        lastName = "Иванов",
        firstName = "Иван",
        surName = "Иванович",
        gender = Gender.MALE,
        birthDay = null,
        email = "ivanov@school.ru",
        phone = "+71234567890",
        dateRegistration = Date(),
        accountStatus = AccountStatus.ACTIVE,
        roles = listOf(Role.ADMIN, Role.TEACHER)
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
        val roles = _userRoles.filter { it.first == userId }
            .map { RoleSchools(it.second, it.third) }
        return user.copy(roles = roles)
    }

    fun addUser(user: UserInfo, roles: List<RoleSchools>): UserInfo {
        val newId = (_users.maxOfOrNull { it.userId } ?: 0) + 1
        val newUser = user.copy(userId = newId)
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
            // Обновляем роли: удаляем старые и добавляем новые
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