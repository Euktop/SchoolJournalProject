package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentStudentSubjectsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.presentation.common.utils.submitList
import stud.euktop.domain.utils.loger.logger
import javax.inject.Inject

/**
 * Экран списка предметов ученика с успеваемостью.
 *
 * Назначение: отображает для текущего ученика список всех предметов,
 * средний балл и итоговую оценку (если есть).
 *
 * Роли: STUDENT
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
class StudentSubjectsFragment :
    BaseFragment<FragmentStudentSubjectsBinding, StudentSubjectsViewModel, StudentSubjectsState, Unit>(),
    ToolbarConfigProvider {
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentSubjectsBinding.inflate(inflater, container, false)

    override val viewModel: StudentSubjectsViewModel by viewModels()

    @Inject
    internal lateinit var router: RouterStudent

    override fun setupUI() {
        binding.rvSubjects.adapter = StudentSubjectAdapter { item ->
            router.toStudentSubjectDetail(item.subjectId)
        }
    }

     override fun updateState(state: StudentSubjectsState) {
         logger?.d(this::class.java.simpleName, "updateState", "subjects count: ${state.subjects.size}")
         binding.rvSubjects.submitList(state.subjects)
     }

    override fun updateEvent(event: Unit) {}
    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = stud.euktop.uikit.R.string.my_subjects,
    )
}