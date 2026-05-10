package stud.euktop.schooljournal.presentation.main.admin.lessons

import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.schooljournal.presentation.common.base.BaseState
import java.util.Date

data class LessonEditState(
    override val isLoading: Boolean = false,
    val lessonId: Int = 0,
    val selectedClass: ClassInfo? = null,
    val selectedSubject: Subject? = null,
    val selectedTeacher: UserProfile? = null,
    val date: Date? = null,
    val topic: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val selectedRoom: Room? = null,
    val locationAddress: String = "",
    val availableClasses: List<ClassInfo> = emptyList(),
    val availableSubjects: List<Subject> = emptyList(),
    val availableTeachers: List<UserProfile> = emptyList(),
    val availableRooms: List<Room> = emptyList()
) : BaseState<LessonEditState>() {

    fun isEditMode() = lessonId != 0

    fun isFormValid(): Boolean {
        return selectedClass != null &&
                selectedSubject != null &&
                selectedTeacher != null &&
                date != null &&
                startTime.isNotBlank() &&
                endTime.isNotBlank()
    }

    override fun updateIsLoading(isLoading: Boolean) = copy(isLoading = isLoading)
}