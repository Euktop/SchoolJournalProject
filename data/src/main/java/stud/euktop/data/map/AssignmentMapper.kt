package stud.euktop.data.map

import com.schooljournal.model.CreateTeacherAssignmentRequest
import com.schooljournal.model.GetTeacherAssignmentsResult
import com.schooljournal.model.UpdateTeacherAssignmentResult
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import java.util.Date

internal fun GetTeacherAssignmentsResult.toAssignmentId(): AssignmentId = AssignmentId(
    teacherId = teacherId ?: 0,
    classId = classId ?: 0,
    subjectId = subjectId ?: 0,
    validFrom = validFrom?.toDate() ?: Date()
)

internal fun GetTeacherAssignmentsResult.toTeacherAssignment(): TeacherAssignment =
    TeacherAssignment(
        assignmentId = toAssignmentId(),
        validToDate = validTo?.toDate(),
        isPrimary = isPrimary ?: false
    )

internal fun CreateTeacherAssignmentRequest.fromDomain(assignment: TeacherAssignment): CreateTeacherAssignmentRequest =
    CreateTeacherAssignmentRequest(
        teacherId = assignment.assignmentId.teacherId,
        classId = assignment.assignmentId.classId,
        subjectId = assignment.assignmentId.subjectId,
        validFrom = assignment.assignmentId.validFrom.toLocalDateTime(),
        validTo = assignment.validToDate?.toLocalDateTime(),
        isPrimary = assignment.isPrimary,
        comment = null
    )

internal fun UpdateTeacherAssignmentResult.toTeacherAssignment(): TeacherAssignment =
    TeacherAssignment(
        assignmentId = AssignmentId(
            teacherId = teacherId ?: 0,
            classId = classId ?: 0,
            subjectId = subjectId ?: 0,
            validFrom = validFrom?.toDate() ?: Date()
        ),
        validToDate = validTo?.toDate(),
        isPrimary = isPrimary ?: false
    )