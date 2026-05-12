package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterError {
    fun onUnauthorized(): () -> Unit
    fun onAccessDenied(): () -> Unit
    fun onNetworkConnection(): () -> Unit
    fun onServerError(): () -> Unit
    fun onNotFound(): () -> Unit
    fun onConflict(): () -> Unit
    fun onSessionExpired(): () -> Unit
    fun onInvalidCredentials(): () -> Unit
    fun onUserBlocked(): () -> Unit
    fun onUserDeleted(): () -> Unit
    fun onFieldRequired(): () -> Unit
    fun onRecordNotFound(): () -> Unit
    fun onUniqueViolation(): () -> Unit
    fun onForeignKeyViolation(): () -> Unit
    fun onRecordAlreadyFixed(): () -> Unit
    fun onTeacherCannotTeachSubject(): () -> Unit
    fun onStudentNotInClass(): () -> Unit
    fun onNoPermissionToGrade(): () -> Unit
    fun onNoPermissionToManageUser(): () -> Unit
    fun onCannotDeleteClassWithStudents(): () -> Unit
    fun onCannotDeleteClassWithLessons(): () -> Unit
    fun onRoomNotBelongsToSchool(): () -> Unit
    fun onTeacherTimeConflict(): () -> Unit
    fun onClassTimeConflict(): () -> Unit
    fun onStartTimeAfterEndTime(): () -> Unit
    fun onCannotDeleteSchoolWithClasses(): () -> Unit
    fun onCannotDeleteRoomUsedInLessons(): () -> Unit
    fun onClassTimeOverlap(): () -> Unit
    fun onMultiplePrimaryTeachers(): () -> Unit
    fun onPermissionOutOfRange(): () -> Unit
    fun onDefault(): () -> Unit
}