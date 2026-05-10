package stud.euktop.schooljournal.presentation.common.filter.classes

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolFilter
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseFilterViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class ClassFilterViewModel @Inject constructor(
    private val schoolAdminRepository: SchoolAdminRepository,
    private val userAdminRepository: UserAdminRepository,
    coordinatorExec: CoordinatorExec
) : BaseFilterViewModel(coordinatorExec) {

    private val _schools = MutableStateFlow<List<School>>(emptyList())
    val schools: StateFlow<List<School>> = _schools

    private val _teachers = MutableStateFlow<List<UserProfile>>(emptyList())
    val teachers: StateFlow<List<UserProfile>> = _teachers

    fun loadSchools(filter: SchoolFilter = SchoolFilter()) {
        execSync({ schoolAdminRepository.getSchools(filter) }) { schools ->
            _schools.value = schools
        }
    }

    fun loadTeachers(userFilter: UserFilter) {
        execSync({ userAdminRepository.getUsers(userFilter) }) { teachers ->
            _teachers.value = teachers
        }
    }
}