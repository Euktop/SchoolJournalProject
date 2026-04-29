package stud.euktop.schooljournal.presentation.main.teacher.teacherLessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherLessonsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.main.teacher.lessonMarks.LessonMarksViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TeacherLessonsFragment : BaseFragment<
        FragmentTeacherLessonsBinding,
        TeacherLessonsViewModel,
        TeacherLessonsState,
        Unit
        >() {
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherLessonsBinding.inflate(i, c, false)

    @Inject
    lateinit var navigationManager: NavigationManager
    override val viewModel: TeacherLessonsViewModel by viewModels()
    private var adapter: TeacherLessonsAdapter? = null

    override fun setupUI() {
        adapter = TeacherLessonsAdapter { lesson ->
            navigationManager.navigate(
                NavCommand.ToDestination(
                    destId = R.id.lessonMarksFragment,
                    args = Bundle().apply {
                        putInt(LessonMarksViewModel.LESSON_ID_KEY, lesson.lessonId)
                    }
                )
            )
        }
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = adapter
    }

    override fun updateState(state: TeacherLessonsState) {
        adapter?.submitList(state.lessons)
    }

    override fun updateEvent(event: Unit) {}
}