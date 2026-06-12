package stud.euktop.schooljournal.presentation.main.teacher.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentTeacherDashboardBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider

import stud.euktop.schooljournal.R as RApp
import stud.euktop.uikit.R as RUi

@AndroidEntryPoint
class TeacherDashboardFragment : BaseFragment<
        FragmentTeacherDashboardBinding,
        TeacherDashboardViewModel,
        TeacherDashboardState,
        Unit>(), ToolbarConfigProvider {

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
        binding.tvWelcomeTitle.text = if (state.teacherName.isNotEmpty()) {
            getString(RApp.string.Hello_teacher_name, state.teacherName)
        } else {
            getString(RApp.string.hello_teacher)
        }

        // 2. Информация о следующем уроке
        binding.tvNextLesson.text = state.nextLessonInfo.ifEmpty { getString(RApp.string.def_teacher_dashboard_name) }
    }

    override fun updateEvent(event: Unit) {}

    override fun getToolbarConfig() = ToolbarConfig(
        titleRes = RUi.string.app_name,
        menuRes = RApp.menu.menu_home
    )
}

