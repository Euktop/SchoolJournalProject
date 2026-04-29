package stud.euktop.data

import kotlinx.coroutines.delay
import stud.euktop.domain.model.AbsenceTypes
import stud.euktop.domain.model.AccountStatus
import stud.euktop.domain.model.ClassInfo
import stud.euktop.domain.model.Gender
import stud.euktop.domain.model.Profile
import stud.euktop.domain.model.Role
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
        delay(500)
    }

    val classes = listOf(
        TeacherClassItem(1, "Школа №1", 5, "А", 2025, 2026, 1, "Математика"),
        TeacherClassItem(2, "Школа №1", 5, "А", 2025, 2026, 2, "Русский язык"),
        TeacherClassItem(3, "Школа №2", 6, "Б", 2025, 2026, 3, "Физика")
    )

    val lessons = listOf(
        TeacherLessonItem(
            101,
            "28.04.2026",
            "Квадратные уравнения",
            "09:00",
            "09:45",
            "42",
            "Иванова А.А."
        ),
        TeacherLessonItem(
            102,
            "27.04.2026",
            "Дискриминант",
            "10:00",
            "10:45",
            "42",
            "Иванова А.А."
        ),
        TeacherLessonItem(
            103,
            "26.04.2026",
            "Введение",
            "08:15",
            "09:00",
            "Спортзал",
            "Петров Б.Б."
        )
    )

    val marks = listOf(
        StudentMarkItem(1, "Алексеев", "Артём", "Андреевич", AbsenceTypes.G5, "Молодец"),
        StudentMarkItem(2, "Борисова", "Вера", "Владимировна", AbsenceTypes.G4, null),
        StudentMarkItem(3, "Григорьев", "Денис", "Евгеньевич", AbsenceTypes.ILL, "Болеет"),
        StudentMarkItem(4, "Дмитриева", "Елена", null, AbsenceTypes.G3, null)
    )
    val adminUsers = listOf(
        UserInfo(
            userId = 1,
            lastName = "Иванов",
            firstName = "Иван",
            surName = "Иванович",
            email = "ivanov@school.ru",
            phone = "+71234567890",
            roleNames = listOf("Администратор"),
            accountStatus = AccountStatus.ACTIVE
        ),
        UserInfo(
            userId = 2,
            lastName = "Петрова",
            firstName = "Анна",
            surName = "Сергеевна",
            email = "petrova@school.ru",
            phone = null,
            roleNames = listOf("Учитель"),
            accountStatus = AccountStatus.ACTIVE
        ),
        UserInfo(
            userId = 3,
            lastName = "Сидоров",
            firstName = "Алексей",
            surName = null,
            email = "sidorov@school.ru",
            phone = null,
            roleNames = listOf("Ученик"),
            accountStatus = AccountStatus.ACTIVE
        )
    )

    val adminClasses = listOf(
        ClassInfo(1, "Школа №1", 5, "А", 2025, 2026, 1, "Математика"),
        ClassInfo(2, "Школа №1", 5, "Б", 2025, 2026, 2, "Русский язык")
    )

    val adminSubjects = listOf(
        Subject(1, "Математика", "Алгебра и геометрия"),
        Subject(2, "Русский язык", null),
        Subject(3, "Физика", null)
    )

    val adminAssignments = listOf(
        TeacherAssignment(2, "Петрова А.С.", 1, "5А", 1, "Математика", "01.09.2025", null),
        TeacherAssignment(1, "Иванов И.И.", 2, "5Б", 2, "Русский язык", "01.09.2025", "30.05.2026")
    )

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
}