package stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentStudentSubjectDetailBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.uikit.components.lineChart.SchJLineChartState
import stud.euktop.uikit.components.lineChart.SchJLinePoint
/**
 * Детальный экран успеваемости по предмету (ученик).
 *
 * Назначение: отображает динамику успеваемости (график) и список всех оценок/пропусков
 * по выбранному предмету.
 *
 * Роли: STUDENT, PARENT
 *
 * Функционал:
 * - Получение subjectId из аргументов навигации
 * - Загрузка списка оценок через StudentRepository.getSubjectMarks(studentId, subjectId)
 * - Построение линейного графика (SchJLineChart) по датам (значения 2-5, пропуски = 0)
 * - Отображение списка оценок в RecyclerView: дата, оценка/код пропуска, комментарий
 * - Обработка пустого состояния (если нет оценок)
 *
 * @see StudentSubjectDetailViewModel
 */
//stud.euktop.schooljournal.presentation.main.student.studentSubjectDetail.StudentSubjectDetailFragment
@AndroidEntryPoint
class StudentSubjectDetailFragment : BaseFragment<
        FragmentStudentSubjectDetailBinding,
        StudentSubjectDetailViewModel,
        StudentSubjectDetailState,
        Unit>() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentStudentSubjectDetailBinding.inflate(i, c, false)

    override val viewModel: StudentSubjectDetailViewModel by viewModels()
    private var marksAdapter: StudentMarkAdapter? = null

    override fun setupUI() {
        marksAdapter = StudentMarkAdapter { /* клик по оценке не нужен */ }
        binding.rvMarks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMarks.adapter = marksAdapter
    }

    override fun updateState(state: StudentSubjectDetailState) {
        // Обновляем график
        binding.lcProgress.state = SchJLineChartState(
            points = state.marks.mapIndexed { _, mark ->
                SchJLinePoint(
                    label = mark.date.substring(0, 5), // первые 5 символов даты "28.04"
                    value = mark.value?.toFloat() ?: 0f
                )
            }
        )
        // Обновляем список оценок
        marksAdapter?.submitList(state.marks)
    }

    override fun updateEvent(event: Unit) {}
}