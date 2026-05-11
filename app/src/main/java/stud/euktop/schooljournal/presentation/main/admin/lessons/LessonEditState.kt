package stud.euktop.schooljournal.presentation.main.admin.lessons

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserListItem
import stud.euktop.schooljournal.presentation.common.base.BaseState
import java.util.Date

data class LessonEditState(
    val lessonId: Int = 0,
    val selectedClass: ClassInfo? = null,
    val selectedSubject: Subject? = null,
    val selectedTeacher: UserListItem? = null,
    val date: Date? = null,
    val topic: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val selectedRoom: Room? = null,
    val locationAddress: String = "",
    val availableClasses: List<ClassInfo> = emptyList(),
    val availableSubjects: List<Subject> = emptyList(),
    val availableTeachers: List<UserListItem> = emptyList(),
    val availableRooms: List<Room> = emptyList(),
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<LessonEditState>() {
    fun isFormValid(): Boolean =
        selectedClass != null && selectedSubject != null && selectedTeacher != null && date != null && startTime.isNotBlank() && endTime.isNotBlank()
    override fun updateIsLoading(loadingMap: Map<String, Boolean>): LessonEditState {
        return copy(loadingMap = loadingMap)
    }
}