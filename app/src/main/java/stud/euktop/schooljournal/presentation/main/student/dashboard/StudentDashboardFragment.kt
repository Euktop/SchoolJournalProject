package stud.euktop.schooljournal.presentation.main.student.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.databinding.FragmentStudentDashboardBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.R as RApp
import stud.euktop.uikit.R as RUi

@AndroidEntryPoint
class StudentDashboardFragment :
    BaseFragment<FragmentStudentDashboardBinding, StudentDashboardViewModel, StudentDashboardState, StudentDashboardEvent>(),
    ToolbarConfigProvider {

    override val viewModel: StudentDashboardViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentDashboardBinding.inflate(inflater, container, false)

    override fun setupUI() {
        // Клики делегируются в ViewModel — он эмиттит event, фрагмент обработает и переключит таб.
        binding.btnSchedule.setOnClickListener { viewModel.onScheduleClick() }
        binding.btnSubjects.setOnClickListener { viewModel.onSubjectsClick() }
        binding.btnHomework.setOnClickListener { viewModel.onHomeworkClick() }
        binding.cardWelcome.setOnClickListener { viewModel.onProfileClick() }
    }

    override fun updateState(state: StudentDashboardState) {
        logger?.d(
            this::class.java.simpleName,
            "updateState",
            "studentName: ${state.studentName}, newHomeworks: ${state.newHomeworksCount}, subjects: ${state.subjectsCount}"
        )
        // 1. Приветствие
        binding.tvWelcomeTitle.text = if (!state.studentName.isNullOrEmpty()) {
            getString(RUi.string.hello_student_format, state.studentName)
        } else {
            getString(RUi.string.hello_student)
        }

        // 2. Статистика
        binding.tvNewHomeworksCount.text = state.newHomeworksCount.toString()
        binding.tvAverageMark.text = state.averageMark?.let { String.format("%.1f", it) } ?: "—"

        // Форматирование с учетом множественных чисел (Plurals)
        binding.tvScheduleSubtitle.text = resources.getQuantityString(
            RUi.plurals.lessons_count, state.lessonsTodayCount, state.lessonsTodayCount
        )
        binding.tvSubjectsSubtitle.text = resources.getQuantityString(
            RUi.plurals.subjects_count, state.subjectsCount, state.subjectsCount
        )
        binding.tvHomeworkSubtitle.text = resources.getQuantityString(
            RUi.plurals.homeworks_not_submitted,
            state.homeworksNotSubmitted,
            state.homeworksNotSubmitted
        )

        // 3. Следующий урок
        binding.cardNextLesson.visibility =
            if (state.isNextLessonVisible) View.VISIBLE else View.GONE
        if (state.isNextLessonVisible) {
            binding.tvLessonName.text = state.nextLessonName

            // TODO: Рефакторинг - передавать parsedRoom и parsedTeacher в State отдельно
            // TODO: Вместо парсинга строки здесь используются state.nextLessonDetails.split("|")
            val parts = state.nextLessonDetails.split("|")
            val room = parts.getOrNull(0)?.takeIf { it.isNotEmpty() }
                ?.let { getString(RUi.string.cabinet_format, it) }
            val teacher = parts.getOrNull(1)?.takeIf { it.isNotEmpty() }
                ?.let { getString(RUi.string.professor_format, it) }
            binding.tvLessonDetails.text =
                listOfNotNull(room, teacher).joinToString(getString(RUi.string.separator_bullet))

            binding.tvTimeBadge.text = state.nextLessonTime?.let { pair ->
                val args = pair.second.filterNotNull().toTypedArray()
                if (args.isEmpty()) {
                    context?.getString(pair.first) ?: ""
                } else {
                    context?.getString(pair.first, *args) ?: ""
                }
            } ?: ""
        }
    }

    override fun updateEvent(event: StudentDashboardEvent) {
        val ctrl = parentFragment as? stud.euktop.schooljournal.presentation.MainController
        when (event) {
            StudentDashboardEvent.SwitchToSchedule -> ctrl?.switchToTab(stud.euktop.schooljournal.R.id.studentScheduleFragment)
            StudentDashboardEvent.SwitchToSubjects -> ctrl?.switchToTab(stud.euktop.schooljournal.R.id.studentSubjectsFragment)
            StudentDashboardEvent.SwitchToHomework -> ctrl?.switchToTab(stud.euktop.schooljournal.R.id.studentHomeworkFragment)
        }
    }

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = RUi.string.app_name, menuRes = RApp.menu.menu_home
    )
}