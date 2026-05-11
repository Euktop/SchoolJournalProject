package stud.euktop.schooljournal.presentation.common.filter.assignment

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.model.school.SubjectFilter
import stud.euktop.domain.model.user.Role
import stud.euktop.domain.model.user.UserFilter
import stud.euktop.domain.model.user.UserProfile
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.base.BaseFilterViewModel
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject

@HiltViewModel
class TeacherAssignmentFilterViewModel @Inject constructor(
    private val userRepository: UserAdminRepository,
    private val classRepository: ClassAdminRepository,
    private val subjectRepository: SubjectAdminRepository,
    coordinatorExec: CoordinatorExec
) : BaseFilterViewModel(coordinatorExec) {

    private val _teachers = MutableStateFlow<List<UserProfile>>(emptyList())
    val teachers: StateFlow<List<UserProfile>> = _teachers

    private val _classes = MutableStateFlow<List<ClassInfo>>(emptyList())
    val classes: StateFlow<List<ClassInfo>> = _classes

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects

    fun loadTeachers(filter: UserFilter = UserFilter(role = Role.TEACHER)) {
        execSync<List<UserProfile>>(
            {
                val result = userRepository.getUsers(filter)
                val result2 = result.fold(
                    onSuccess = { it ->
                        it.map { userRepository.getUser(it.userId).getOrNull() }.mapNotNull { it }
                    },
                    onFailure = {
                        return@execSync Result.failure(it)
                    }
                )
                Result.success(result2)
            }) { teachers ->
            _teachers.update { teachers }
        }
    }

    fun loadClasses(filter: ClassInfoFilter = ClassInfoFilter()) {
        execSync({ classRepository.getClasses(filter) }) { classes ->
            _classes.update { classes }
        }
    }

    fun loadSubjects(filter: SubjectFilter = SubjectFilter()) {
        execSync({ subjectRepository.getSubjects(filter) }) { subjects ->
            _subjects.update { subjects }
        }
    }
}