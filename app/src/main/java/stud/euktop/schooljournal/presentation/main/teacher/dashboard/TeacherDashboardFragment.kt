package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
import stud.euktop.schooljournal.databinding.FragmentTeacherDashboardBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.R as RApp
import stud.euktop.uikit.R as RUi

@AndroidEntryPoint
class TeacherDashboardFragment :
    BaseFragment<FragmentTeacherDashboardBinding, TeacherDashboardViewModel, TeacherDashboardState, TeacherDashboardEvent>(),
    ToolbarConfigProvider {

    override val viewModel: TeacherDashboardViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherDashboardBinding.inflate(inflater, container, false)

    override fun setupUI() {
        binding.btnSchedule.setOnClickListener { viewModel.onScheduleClick() }
        binding.btnMyClasses.setOnClickListener { viewModel.onMyClassesClick() }
        binding.btnLessonsList.setOnClickListener { viewModel.onLessonsListClick() }
        binding.btnGrading.setOnClickListener { viewModel.onGradingClick() }
        binding.btnHomeworkList.setOnClickListener { viewModel.onHomeworkListClick() }
        binding.btnCreateHomework.setOnClickListener { viewModel.onCreateHomeworkClick() }
        binding.btnAnalytics.setOnClickListener { viewModel.onAnalyticsClick() }
        binding.btnSettings.setOnClickListener { viewModel.onSettingsClick() }
    }

     override fun updateState(state: TeacherDashboardState) {
         logger?.d(this::class.java.simpleName, "updateState", "teacherName: ${state.teacherName}, lessonsCount: ${state.lessonsCount}, nextLesson: ${state.nextLessonInfo}")
         binding.tvWelcomeTitle.text = if (state.teacherName.isNotEmpty()) {
             getString(RUi.string.hello_teacher_name, state.teacherName)
         } else {
             getString(RUi.string.hello_teacher)
         }

         binding.tvNextLesson.text = state.nextLessonInfo.ifEmpty {
             getString(RUi.string.def_teacher_dashboard_name)
         }

         binding.tvLessonsToday.text = resources.getQuantityString(
             RUi.plurals.lessons_count, state.lessonsCount, state.lessonsCount
         )
     }

    override fun updateEvent(event: TeacherDashboardEvent) {
        val ctrl = parentFragment as? stud.euktop.schooljournal.presentation.MainController
        when (event) {
            TeacherDashboardEvent.SwitchToSchedule -> ctrl?.switchToTab(stud.euktop.schooljournal.R.id.teacherLessonsFragment)
            TeacherDashboardEvent.SwitchToClasses -> ctrl?.switchToTab(stud.euktop.schooljournal.R.id.teacherClassesFragment)
            TeacherDashboardEvent.SwitchToHomework -> ctrl?.switchToTab(stud.euktop.schooljournal.R.id.teacherHomeworkListFragment)
        }
    }

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = RUi.string.app_name, menuRes = RApp.menu.menu_home
    )
}

