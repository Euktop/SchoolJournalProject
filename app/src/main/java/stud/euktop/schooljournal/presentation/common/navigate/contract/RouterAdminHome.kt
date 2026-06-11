package stud.euktop.schooljournal.presentation.common.navigate.contract

/**
 * Роутер для главного экрана администратора (AdminHomeFragment).
 * Отвечает за навигацию по пунктам Drawer-меню и Quick Actions.
 */
interface RouterAdminHome {
    fun toDashboard()
    fun toSchoolsList()
    fun toSchoolEdit(schoolId: Int)
    fun toClassesList()
    fun toSubjectsList()
    fun toUsersList()
    fun toSettings()
    fun toGenerateReport()
    fun toInviteUsers()
    fun toMaintenance()
}