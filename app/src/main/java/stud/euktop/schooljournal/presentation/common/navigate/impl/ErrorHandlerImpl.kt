package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.domain.model.common.DataError
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.ErrorHandler
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandlerImpl @Inject constructor(
    private val routerError: RouterError
) : ErrorHandler {

    override suspend fun exec(throwable: Throwable): CoordinatorResult.Error {
        val (action, msgId) = when (throwable) {
            is DataError.NetworkConnection -> routerError.onNetworkConnection() to R.string.error_network
            is DataError.Unauthorized -> routerError.onUnauthorized() to R.string.error_auth_expired
            is DataError.AccessDenied -> routerError.onAccessDenied() to R.string.error_no_permission
            is DataError.SessionContextNotSet -> routerError.onSessionExpired() to R.string.error_auth_expired
            is DataError.InvalidCredentials -> routerError.onInvalidCredentials() to R.string.error_invalid_credentials
            is DataError.UserNotFound -> routerError.onRecordNotFound() to R.string.error_user_not_found
            is DataError.UserDeleted -> routerError.onUserDeleted() to R.string.error_user_deleted
            is DataError.UserNotConfirmed -> routerError.onDefault() to R.string.error_user_not_confirmed
            is DataError.UserBlocked -> routerError.onUserBlocked() to R.string.error_user_blocked
            is DataError.RecordAlreadyFixed -> routerError.onRecordAlreadyFixed() to R.string.error_record_already_fixed
            is DataError.TeacherCannotTeachSubject -> routerError.onTeacherCannotTeachSubject() to R.string.error_teacher_cannot_teach
            is DataError.StudentNotInClass -> routerError.onStudentNotInClass() to R.string.error_student_not_in_class
            is DataError.NoPermissionToGrade -> routerError.onNoPermissionToGrade() to R.string.error_no_permission_to_grade
            is DataError.FieldRequired -> routerError.onFieldRequired() to R.string.error_field_required
            is DataError.UserNotExist -> routerError.onRecordNotFound() to R.string.error_user_not_found
            is DataError.NoPermissionToManageUser -> routerError.onNoPermissionToManageUser() to R.string.error_no_permission_to_manage_user
            is DataError.CannotDeleteClassWithStudents -> routerError.onCannotDeleteClassWithStudents() to R.string.error_cannot_delete_class_with_students
            is DataError.CannotDeleteClassWithLessons -> routerError.onCannotDeleteClassWithLessons() to R.string.error_cannot_delete_class_with_lessons
            is DataError.RoomNotBelongsToSchool -> routerError.onRoomNotBelongsToSchool() to R.string.error_room_not_belongs_to_school
            is DataError.TeacherTimeConflict -> routerError.onTeacherTimeConflict() to R.string.error_teacher_time_conflict
            is DataError.ClassTimeConflict -> routerError.onClassTimeConflict() to R.string.error_class_time_conflict
            is DataError.StartTimeAfterEndTime -> routerError.onStartTimeAfterEndTime() to R.string.error_start_time_after_end_time
            is DataError.CannotDeleteSchoolWithClasses -> routerError.onCannotDeleteSchoolWithClasses() to R.string.error_cannot_delete_school_with_classes
            is DataError.CannotDeleteRoomUsedInLessons -> routerError.onCannotDeleteRoomUsedInLessons() to R.string.error_cannot_delete_room_used_in_lessons
            is DataError.ClassTimeOverlap -> routerError.onClassTimeOverlap() to R.string.error_class_time_overlap
            is DataError.MultiplePrimaryTeachers -> routerError.onMultiplePrimaryTeachers() to R.string.error_multiple_primary_teachers
            is DataError.PermissionOutOfRange -> routerError.onPermissionOutOfRange() to R.string.error_permission_out_of_range
            is DataError.RecordNotFound -> routerError.onRecordNotFound() to R.string.error_record_not_found
            is DataError.SchoolNotFound -> routerError.onNotFound() to R.string.error_school_not_found
            is DataError.ClassNotFound -> routerError.onNotFound() to R.string.error_class_not_found
            is DataError.SubjectNotFound -> routerError.onNotFound() to R.string.error_subject_not_found
            is DataError.TeacherNotFound -> routerError.onNotFound() to R.string.error_teacher_not_found
            is DataError.UniqueViolation -> routerError.onUniqueViolation() to R.string.error_unique_violation
            is DataError.ForeignKeyViolation -> routerError.onForeignKeyViolation() to R.string.error_foreign_key_violation
            is DataError.RecordAlreadyExists -> routerError.onConflict() to R.string.error_record_already_exists
            is DataError.InternalServerError -> routerError.onServerError() to R.string.error_server
            is DataError.HttpError -> when (throwable.code) {
                401 -> routerError.onUnauthorized() to R.string.error_auth_expired
                403 -> routerError.onAccessDenied() to R.string.error_no_permission
                404 -> routerError.onNotFound() to R.string.error_not_found
                409 -> routerError.onConflict() to R.string.error_conflict
                in 500..599 -> routerError.onServerError() to R.string.error_server
                else -> routerError.onDefault() to R.string.error_http
            }
            is DataError.EmptyBody -> routerError.onDefault() to R.string.error_empty_body
            else -> routerError.onDefault() to R.string.error_unknown
        }
        logger?.e("ErrorHandler", "Error occurred", throwable)
        return CoordinatorResult.Error(action, msgId)
    }
}