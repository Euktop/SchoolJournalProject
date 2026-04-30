// data/src/main/java/stud/euktop/data/MockData.kt
package stud.euktop.data

import kotlinx.coroutines.delay
import stud.euktop.domain.model.AbsenceTypes
import stud.euktop.domain.model.AccountStatus
import stud.euktop.domain.model.ClassInfo
import stud.euktop.domain.model.Gender
import stud.euktop.domain.model.Profile
import stud.euktop.domain.model.Role
import stud.euktop.domain.model.RoleSchools
import stud.euktop.domain.model.School
import stud.euktop.domain.model.StudentMarkItem
import stud.euktop.domain.model.StudentSubjectMark
import stud.euktop.domain.model.StudentSubjectSummary
import stud.euktop.domain.model.Subject
import stud.euktop.domain.model.TeacherAssignment
import stud.euktop.domain.model.TeacherClassItem
import stud.euktop.domain.model.TeacherLessonItem
import stud.euktop.domain.model.UserInfo
import java.util.Date

internal object MockData {

    suspend fun delay() {
        delay(250)
    }

    // ==================== ШКОЛЫ ====================
    val schools = mutableListOf<School>().apply {
        add(School(1, "Школа №1", "Регион 1", "Адрес 1"))
        add(School(2, "Школа №2", "Регион 2", "Адрес 2"))
    }

    // ==================== ПОЛЬЗОВАТЕЛИ И РОЛИ ====================
    val users = mutableListOf<UserInfo>().apply {
        add(
            UserInfo(
                userId = 1,
                lastName = "Иванов",
                firstName = "Иван",
                surName = "Иванович",
                email = "ivanov@school.ru",
                phone = "+71234567890",
                roles = listOf(RoleSchools(Role.ADMIN, null)),
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
                roles = listOf(RoleSchools(Role.TEACHER, 1)),
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
                roles = listOf(RoleSchools(Role.STUDENT, 1)),
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
                roles = listOf(RoleSchools(Role.STUDENT, 1)),
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
                roles = listOf(RoleSchools(Role.STUDENT, 1)),
                accountStatus = AccountStatus.ACTIVE
            )
        )
    }

    val usersRoles = mutableListOf<Triple<Int, Role, Int?>>().apply {
        addAll(users.flatMap { user ->
            user.roles.map { roleSchool ->
                Triple(user.userId, roleSchool.role, roleSchool.schoolId)
            }
        })
    }

    // ==================== УЧИТЕЛЯ (TeacherClassItem для экрана учителя) ====================
    val teacherClasses = listOf(
        TeacherClassItem(1, "Школа №1", 5, "А", 2025, 2026, 1, "Математика"),
        TeacherClassItem(2, "Школа №1", 5, "А", 2025, 2026, 2, "Русский язык"),
        TeacherClassItem(3, "Школа №2", 6, "Б", 2025, 2026, 3, "Физика")
    )

    val lessons = listOf(
        TeacherLessonItem(
            lessonId = 101,
            date = "28.04.2026",
            topic = "Квадратные уравнения",
            startTime = "09:00",
            endTime = "09:45",
            roomName = "42",
            teacherName = "Иванова А.А."
        ),
        TeacherLessonItem(
            lessonId = 102,
            date = "27.04.2026",
            topic = "Дискриминант",
            startTime = "10:00",
            endTime = "10:45",
            roomName = "42",
            teacherName = "Иванова А.А."
        ),
        TeacherLessonItem(
            lessonId = 103,
            date = "26.04.2026",
            topic = "Введение",
            startTime = "08:15",
            endTime = "09:00",
            roomName = "Спортзал",
            teacherName = "Петров Б.Б."
        )
    )

    val marks = listOf(
        StudentMarkItem(4, "Борисова", "Вера", "Владимировна", AbsenceTypes.G5, "Молодец"),
        StudentMarkItem(5, "Дмитриева", "Елена", null, AbsenceTypes.G4, null),
        StudentMarkItem(3, "Сидоров", "Алексей", null, AbsenceTypes.ILL, "Болеет"),
        StudentMarkItem(2, "Петрова", "Анна", "Сергеевна", AbsenceTypes.G3, null)
    )

    // ==================== ДЛЯ CRUD КЛАССОВ ====================
    val classInfos = mutableListOf<ClassInfo>().apply {
        add(
            ClassInfo(
                classId = 1,
                schoolId = 1,
                schoolName = "Школа №1",
                grade = 5,
                letter = "А",
                academicYearStart = 2025,
                academicYearEnd = 2026,
                teacherId = 2,
                teacherName = "Петрова А.С."
            )
        )
        add(
            ClassInfo(
                classId = 2,
                schoolId = 1,
                schoolName = "Школа №1",
                grade = 5,
                letter = "Б",
                academicYearStart = 2025,
                academicYearEnd = 2026,
                teacherId = 1,
                teacherName = "Иванов И.И."
            )
        )
    }

    // ==================== ПРЕДМЕТЫ ====================
    val subjectInfos = mutableListOf<Subject>().apply {
        add(Subject(1, "Математика", "Алгебра и геометрия"))
        add(Subject(2, "Русский язык", null))
        add(Subject(3, "Физика", null))
    }

    // ==================== НАЗНАЧЕНИЯ УЧИТЕЛЕЙ ====================
    val assignmentInfos = mutableListOf<TeacherAssignment>().apply {
        add(
            TeacherAssignment(
                teacherId = 2,
                teacherName = "Петрова А.С.",
                classId = 1,
                className = "5А",
                subjectId = 1,
                subjectName = "Математика",
                validFrom = "01.09.2025",
                validTo = null
            )
        )
        add(
            TeacherAssignment(
                teacherId = 1,
                teacherName = "Иванов И.И.",
                classId = 2,
                className = "5Б",
                subjectId = 2,
                subjectName = "Русский язык",
                validFrom = "01.09.2025",
                validTo = "30.05.2026"
            )
        )
    }

    // ==================== ДАННЫЕ ДЛЯ УЧЕНИКА ====================
    val studentSubjectsSummary = listOf(
        StudentSubjectSummary(1, "Математика", 4.5, 5, "Иванова А.А."),
        StudentSubjectSummary(2, "Русский язык", 3.8, 4, "Петрова С.С."),
        StudentSubjectSummary(3, "Физика", 4.2, 4, "Сидоров В.В.")
    )

    val studentSubjectMarks = listOf(
        StudentSubjectMark("25.04.2026", 4, null, "Хорошо"),
        StudentSubjectMark("22.04.2026", 5, null, "Молодец"),
        StudentSubjectMark("18.04.2026", null, "н", "Без причины"),
        StudentSubjectMark("15.04.2026", 3, null, null)
    )

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
        accountStatusId = AccountStatus.ACTIVE,
        roles = listOf(Role.ADMIN, Role.TEACHER)
    )
    // data/src/main/java/stud/euktop/data/MockData.kt (дополнение)

    // === ДЛЯ ПРЕДМЕТОВ (CRUD) ===
    val subjects = mutableListOf<Subject>().apply {
        add(Subject(1, "Математика", "Алгебра и геометрия"))
        add(Subject(2, "Русский язык", null))
        add(Subject(3, "Физика", null))
    }
}