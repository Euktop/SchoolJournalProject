package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentTeacherLessonsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.lesson.LessonFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
import stud.euktop.schooljournal.presentation.common.utils.submitList
import javax.inject.Inject

@AndroidEntryPoint
class TeacherLessonsFragment : BaseFragment<
        FragmentTeacherLessonsBinding,
        TeacherLessonsViewModel,
        TeacherLessonsState,
        Unit>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherLessonsBinding.inflate(inflater, container, false)

    @Inject
    lateinit var router: RouterTeacher

    override val viewModel: TeacherLessonsViewModel by viewModels()

    override fun setupUI() {
        binding.rvLessons.adapter = TeacherLessonsAdapter { lesson ->
            router.toLessonMarks(lesson.lessonId)
        }
        binding.toolbar.showFilterDialog = { showFilterDialog() }
    }

    private fun showFilterDialog() {
        if (parentFragmentManager.findFragmentByTag("lesson_filter") != null) return
        val dialog = LessonFilterDialog(
            initialFilter = viewModel.state.value.filter,
            onFilterApplied = { filter -> viewModel.applyFilter(filter) },
            onError = viewModel.onError
        )
        dialog.show(parentFragmentManager, "lesson_filter")
    }

    override fun updateState(state: TeacherLessonsState) {
        binding.rvLessons.submitList(state.lessons)
    }

    override fun updateEvent(event: Unit) {}
}