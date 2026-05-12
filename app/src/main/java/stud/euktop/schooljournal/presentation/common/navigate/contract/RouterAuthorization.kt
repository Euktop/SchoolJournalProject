package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterAuthorization {
    suspend fun toCreateProfile()
    suspend fun toCreatePassword()
    suspend fun toCancelCreatePassword()
    suspend fun toLogin()
    suspend fun toSuccessCreate()
    suspend fun toSuccessChangePassword()
    suspend fun toCancelChangePassword()
}