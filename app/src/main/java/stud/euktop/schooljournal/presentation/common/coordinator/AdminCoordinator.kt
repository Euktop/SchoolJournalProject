package stud.euktop.schooljournal.presentation.common.coordinator

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import stud.euktop.domain.model.assignment.AssignmentId
import stud.euktop.domain.model.assignment.TeacherAssignment
import stud.euktop.domain.model.assignment.TeacherAssignmentUpdate
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.ClassInfoUpdate
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.school.SubjectUpdate
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserRole
import stud.euktop.domain.model.user.UserUpdate
import stud.euktop.domain.repository.AssignmentAdminRepository
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.paging.ClassesPagingSource
import stud.euktop.schooljournal.presentation.common.paging.SchoolsPagingSource
import stud.euktop.schooljournal.presentation.common.paging.SubjectsPagingSource
import stud.euktop.schooljournal.presentation.common.paging.UsersPagingSource
import stud.euktop.schooljournal.presentation.main.admin.assignments.TeacherAssignmentFull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminCoordinator @Inject constructor(
    private val userRepository: UserAdminRepository,
    private val schoolRepository: SchoolAdminRepository,
    private val classRepository: ClassAdminRepository,
    private val subjectRepository: SubjectAdminRepository,
    private val assignmentRepository: AssignmentAdminRepository,
    private val coordinatorExec: CoordinatorExec
) {

    // ========== Школы ==========
    fun getSchoolsPagingDataFlow(filter: SchoolFilter): Flow<PagingData<School>> =
        Pager(PagingConfig(pageSize = 20)) {
            SchoolsPagingSource(schoolRepository, filter)
        }.flow

    // ========== Пользователи ==========
    fun getUsersPagingDataFlow(filter: UserFilter): Flow<PagingData<UserProfile>> =
        Pager(PagingConfig(pageSize = 20)) {
            UsersPagingSource(userRepository, filter)
        }.flow

    suspend fun getUser(userId: Int): CoordinatorResult<UserProfile> =
        coordinatorExec.exec { userRepository.getUser(userId) }

    suspend fun addUser(profile: UserProfile, password: String?): CoordinatorResult<UserProfile> =
        coordinatorExec.exec { userRepository.addUser(profile, password) }

    suspend fun updateUser(update: UserUpdate): CoordinatorResult<UserProfile> =
        coordinatorExec.exec { userRepository.updateUser(update) }

    suspend fun deleteUser(userId: Int): CoordinatorResult<Unit> =
        coordinatorExec.exec { userRepository.deleteUser(userId) }

    suspend fun addUserRole(userId: Int, role: Role, schoolId: Int?): CoordinatorResult<UserRole> =
        coordinatorExec.exec { userRepository.addUserRole(userId, role, schoolId) }

    suspend fun removeUserRole(userId: Int, role: Role, schoolId: Int?): CoordinatorResult<Unit> =
        coordinatorExec.exec { userRepository.removeRole(userId, role, schoolId) }

    // ========== Классы ==========
    fun getClassesPagingDataFlow(filter: ClassInfoFilter): Flow<PagingData<ClassInfo>> =
        Pager(PagingConfig(pageSize = 20)) {
            ClassesPagingSource(classRepository, filter)
        }.flow

    suspend fun getClass(classId: Int): CoordinatorResult<ClassInfo> =
        coordinatorExec.exec { classRepository.getClass(classId) }

    suspend fun addClass(classInfo: ClassInfo): CoordinatorResult<ClassInfo> =
        coordinatorExec.exec { classRepository.addClass(classInfo) }

    suspend fun updateClass(update: ClassInfoUpdate): CoordinatorResult<ClassInfo> =
        coordinatorExec.exec { classRepository.updateClass(update) }

    suspend fun deleteClass(classId: Int): CoordinatorResult<Unit> =
        coordinatorExec.exec { classRepository.deleteClass(classId) }

    // ========== Предметы ==========
    fun getSubjectsPagingDataFlow(filter: SubjectFilter): Flow<PagingData<Subject>> =
        Pager(PagingConfig(pageSize = 20)) {
            SubjectsPagingSource(subjectRepository, filter)
        }.flow

    suspend fun addSubject(subject: Subject): CoordinatorResult<Subject> =
        coordinatorExec.exec { subjectRepository.addSubject(subject) }

    suspend fun updateSubject(update: SubjectUpdate): CoordinatorResult<Subject> =
        coordinatorExec.exec { subjectRepository.updateSubject(update) }

    suspend fun deleteSubject(subjectId: Int): CoordinatorResult<Unit> =
        coordinatorExec.exec { subjectRepository.deleteSubject(subjectId) }

    // ========== Назначения учителей ==========
// внутри класса AdminCoordinator

    suspend fun getTeacherAssignmentFull(assignmentId: AssignmentId): CoordinatorResult<TeacherAssignmentFull> =
        coordinatorExec.exec {
            runCatching {
                coroutineScope {
                    val assignment =
                        assignmentRepository.getTeacherAssignment(assignmentId).getOrThrow()
                    val teacherDeferred =
                        async {
                            userRepository.getUser(assignment.assignmentId.teacherId).getOrNull()
                        }
                    val classDeferred =
                        async {
                            classRepository.getClass(assignment.assignmentId.classId).getOrNull()
                        }
                    val subjectDeferred = async {
                        subjectRepository.getSubject(assignment.assignmentId.subjectId).getOrNull()
                    }
                    val teacher = teacherDeferred.await()
                    val classInfo = classDeferred.await()
                    val subject = subjectDeferred.await()
                    TeacherAssignmentFull(
                        assignmentId = assignment.assignmentId,
                        teacher = teacher,
                        classInfo = classInfo,
                        subject = subject,
                        validToDate = assignment.validToDate,
                        isPrimary = assignment.isPrimary
                    )
                }
            }
        }

    suspend fun addTeacherAssignment(assignment: TeacherAssignment): CoordinatorResult<TeacherAssignment> =
        coordinatorExec.exec { assignmentRepository.addTeacherAssignment(assignment) }

    suspend fun updateTeacherAssignment(update: TeacherAssignmentUpdate): CoordinatorResult<TeacherAssignment> =
        coordinatorExec.exec { assignmentRepository.updateTeacherAssignment(update) }

    suspend fun deleteTeacherAssignment(id: AssignmentId): CoordinatorResult<Unit> =
        coordinatorExec.exec { assignmentRepository.deleteTeacherAssignment(id) }
}