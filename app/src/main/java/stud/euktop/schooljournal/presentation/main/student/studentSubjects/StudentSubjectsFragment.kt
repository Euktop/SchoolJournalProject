package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentStudentSubjectsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import stud.euktop.schooljournal.presentation.common.utils.submitList
import javax.inject.Inject

//stud.euktop.schooljournal.presentation.main.student.studentSubjects.StudentSubjectsFragment
/**
 * Экран списка предметов ученика с успеваемостью.
 *
 * Назначение: отображает для текущего ученика список всех предметов,
 * средний балл и итоговую оценку (если есть).
 *
 * Роли: STUDENT, PARENT (для выбранного ребёнка)
 *
 * Функционал:
 * - Загрузка данных через StudentRepository.getSubjectsSummary(studentId)
 * - Отображение в RecyclerView: название предмета, средний балл, итог
 * - При клике на предмет переход к детальному экрану StudentSubjectDetailFragment
 *   с передачей subjectId
 * - Обработка пустого состояния
 *
 * @see StudentSubjectsViewModel
 */
@AndroidEntryPoint
class StudentSubjectsFragment : BaseFragment<
        FragmentStudentSubjectsBinding,
        StudentSubjectsViewModel,
        StudentSubjectsState,
        Unit
        >() {
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentStudentSubjectsBinding.inflate(i, c, false)

    override val viewModel: StudentSubjectsViewModel by viewModels()

    @Inject
    internal lateinit var navigationManager: NavigationManager

    @Inject internal lateinit var router: RouterStudent

    override fun setupUI() {
        binding.rvSubjects.adapter = StudentSubjectAdapter { item ->
            router.toStudentSubjectDetail(item.subjectId)
        }
    }

    override fun updateState(state: StudentSubjectsState) {
        binding.rvSubjects.submitList(state.subjects)
    }

    override fun updateEvent(event: Unit) {}
}