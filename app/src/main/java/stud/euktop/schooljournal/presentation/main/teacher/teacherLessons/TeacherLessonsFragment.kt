package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherLessonsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.filter.lesson.LessonFilterDialog
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.submitList
import stud.euktop.schooljournal.presentation.main.teacher.lessonMarks.LessonMarksViewModel
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
    lateinit var navigationManager: NavigationManager

    override val viewModel: TeacherLessonsViewModel by viewModels()

    override fun setupUI() {
        binding.rvLessons.adapter = TeacherLessonsAdapter { lesson ->
            navigationManager.navigate(
                NavCommand.ToDestination(
                    destId = R.id.lessonMarksFragment,
                    args = Bundle().apply {
                        putInt(LessonMarksViewModel.LESSON_ID_KEY, lesson.lessonId)
                    }
                )
            )
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