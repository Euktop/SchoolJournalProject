package stud.euktop.schooljournal.presentation.common.coordinator

import stud.euktop.domain.model.assignment.TeacherAssignmentFilter
import stud.euktop.domain.repository.AssignmentAdminRepository
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.main.teacher.teacherClass.TeacherClassItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherCoordinator @Inject constructor(
    private val assignmentRepository: AssignmentAdminRepository,
    private val classRepository: ClassAdminRepository,
    private val subjectRepository: SubjectAdminRepository,
    private val coordinatorExec: CoordinatorExec
) {

    suspend fun getTeacherClasses(teacherId: Int): CoordinatorResult<List<TeacherClassItem>> =
        coordinatorExec.exec {
            runCatching {
                val assignments = assignmentRepository.getTeacherAssignments(
                    TeacherAssignmentFilter(teacherId = teacherId)
                ).getOrThrow()
                val distinctPairs = assignments.map { it.assignmentId }
                    .distinctBy { Pair(it.classId, it.subjectId) }
                val result = mutableListOf<TeacherClassItem>()
                for (pair in distinctPairs) {
                    val classInfo = classRepository.getClass(pair.classId).getOrThrow()
                    val subject = subjectRepository.getSubject(pair.subjectId).getOrThrow()
                    result.add(
                        TeacherClassItem(
                            classId = pair.classId,
                            grade = classInfo.grade,
                            letter = classInfo.letter,
                            subjectId = pair.subjectId,
                            subjectName = subject.name
                        )
                    )
                }
                result
            }
        }
}