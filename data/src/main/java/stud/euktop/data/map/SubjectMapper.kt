package stud.euktop.data.map

import com.schooljournal.model.CreateSubjectResult
import com.schooljournal.model.GetStudentMarksBySubjectResult
import com.schooljournal.model.GetStudentSubjectsWithSummaryResult
import com.schooljournal.model.GetSubjectByIdResult
import com.schooljournal.model.GetSubjectsResult
import com.schooljournal.model.UpdateSubjectResult
import stud.euktop.domain.model.attendance.AbsenceTypes
import stud.euktop.domain.model.attendance.StudentSubjectMark
import stud.euktop.domain.model.attendance.StudentSubjectSummary
import stud.euktop.domain.model.school.Subject


private fun toSubject(
    subjectId: Int?,
    name: String?,
    description: String?
) = Subject(
    subjectId = subjectId ?: 0,
    name = name ?: "",
    description = description
)

internal fun GetSubjectsResult.toDomain() = toSubject(
    subjectId = subjectId,
    name = name,
    description = description
)

internal fun GetSubjectByIdResult.toDomain() = toSubject(
    subjectId = subjectId,
    name = name,
    description = description
)

internal fun CreateSubjectResult.toDomain() = toSubject(
    subjectId = subjectId,
    name = name,
    description = description
)

internal fun UpdateSubjectResult.toDomain() = toSubject(
    subjectId = subjectId,
    name = name,
    description = description
)

private fun toSubjectSummary(
    subjectId: Int?,
    name: String?,
    averageMark: Double?,
    marksCount: Int?
) = StudentSubjectSummary(
    subjectId = subjectId ?: 0,
    subjectName = name ?: "",
    averageMark = averageMark,
    finalMark = marksCount
)

internal fun GetStudentSubjectsWithSummaryResult.toDomain() = toSubjectSummary(
    subjectId = subjectId,
    name = name,
    averageMark = averageMark,
    marksCount = marksCount
)

internal fun absenceCodeToAbsenceType(
    value: Int? = null,
    absenceCode: String? = null,
): AbsenceTypes? {
    when (value) {
        2 -> return AbsenceTypes.G2
        3 -> return AbsenceTypes.G3
        4 -> return AbsenceTypes.G4
        5 -> return AbsenceTypes.G5
    }
    when (absenceCode) {
        "б" -> return AbsenceTypes.ILL
        "н" -> return AbsenceTypes.IRRESPECTABLE
        "у" -> return AbsenceTypes.RESPECTABLE
    }
    return null
}

internal fun GetStudentMarksBySubjectResult.toDomain() = StudentSubjectMark(
    gradeId = gradeId ?: 0,
    date = date.toDate(),
    absenceCode = absenceCodeToAbsenceType(value, absenceCode),
    comment = comment
)