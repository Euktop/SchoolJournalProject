package stud.euktop.data.map

import com.schooljournal.model.CreateClassRequest
import com.schooljournal.model.GetClassByIdResult
import com.schooljournal.model.GetClassesResult
import com.schooljournal.model.GetStudentClassResult
import stud.euktop.domain.model.school.ClassInfo

internal fun ClassInfo.toCreateClassRequest(): CreateClassRequest = CreateClassRequest(
    schoolId = schoolId,
    grade = grade,
    letter = letter,
    academicYearStart = academicYearStart,
    academicYearEnd = academicYearEnd,
    classTeacherId = teacherId
)

internal fun GetClassesResult.toClassInfo(): ClassInfo = ClassInfo(
    classId = classId ?: 0,
    schoolId = schoolId ?: 0,
    grade = grade ?: 0,
    letter = letter ?: "",
    academicYearStart = academicYearStart ?: -1,
    academicYearEnd = academicYearEnd ?: -1,
    teacherId = classTeacherId?.takeIf { it != 0 }
)

internal fun GetClassByIdResult.toClassInfo(): ClassInfo = ClassInfo(
    classId = classId ?: 0,
    schoolId = schoolId ?: 0,
    grade = grade ?: 0,
    letter = letter ?: "",
    academicYearStart = academicYearStart ?: -1,
    academicYearEnd = academicYearEnd ?: -1,
    teacherId = classTeacherId?.takeIf { it != 0 }
)

internal fun GetStudentClassResult.toStudentClassInfo(): ClassInfo = ClassInfo(
    classId = classId ?: 0,
    schoolId = schoolId ?: 0,
    grade = grade ?: 0,
    letter = letter ?: "",
    academicYearStart = academicYearStart ?: -1,
    academicYearEnd = academicYearEnd ?: -1,
    teacherId = classTeacherId
)