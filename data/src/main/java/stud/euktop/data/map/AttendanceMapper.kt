package stud.euktop.data.map

import com.schooljournal.model.GetLessonMarksResult
import stud.euktop.domain.model.attendance.StudentMarkItem

internal fun GetLessonMarksResult.toDomain(): StudentMarkItem = StudentMarkItem(
    studentId = studentId ?: 0,
    lastName = lastName ?: "",
    firstName = firstName ?: "",
    surName = surName,
    absenceCode = absenceCode?.toAbsenceType(),
    comment = comment
)