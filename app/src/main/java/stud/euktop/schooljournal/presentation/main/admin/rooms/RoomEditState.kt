package stud.euktop.schooljournal.presentation.main.admin.rooms

import stud.euktop.domain.model.school.School
import stud.euktop.domain.utils.validation.TextThereValidator
import stud.euktop.schooljournal.presentation.common.base.BaseState

data class RoomEditState(
    val roomId: Int = 0,
    val school: School? = null,
    val name: TextThereValidator = TextThereValidator(),
    val originalSchoolId: Int? = null,
    val originalName: String = "",
    override val loadingMap: Map<String, Boolean> = emptyMap()
) : BaseState<RoomEditState>() {
    fun isFormValid(): Boolean = school != null && name.validate()
    override fun updateIsLoading(loadingMap: Map<String, Boolean>) = copy(loadingMap = loadingMap)
}