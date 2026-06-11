// RouterProfile.kt
package stud.euktop.schooljournal.presentation.common.navigate.contract

interface RouterProfile {
    fun toProfile()
    fun toEditUser(userId: Int)
    fun toChangePassword()
    fun toLogout()
}