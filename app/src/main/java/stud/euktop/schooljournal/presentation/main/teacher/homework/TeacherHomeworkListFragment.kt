package stud.euktop.schooljournal.presentation.main.teacher.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherHomeworkListBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.utils.submitList
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeworkListFragment : BaseFragment<
        FragmentTeacherHomeworkListBinding,
        TeacherHomeworkViewModel,
        TeacherHomeworkState,
        TeacherHomeworkEvent>() {

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherHomeworkListBinding.inflate(i, c, false)

    override val viewModel: TeacherHomeworkViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    private lateinit var adapter: TeacherHomeworkAdapter

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            navigationManager.navigate(NavCommand.Back)
        }
        binding.fabAddHomework.setOnClickListener {
            navigationManager.navigate(NavCommand.ToDestination(R.id.teacherHomeworkEditFragment))
        }
        adapter = TeacherHomeworkAdapter { homework ->
            // Переход на экран редактирования с передачей homeworkId
            val bundle = Bundle().apply {
                putInt("homeworkId", homework.homeworkId)
            }
            navigationManager.navigate(
                NavCommand.ToDestination(R.id.teacherHomeworkEditFragment, args = bundle)
            )
        }
        binding.rvHomeworkList.adapter = adapter
    }

    override fun updateState(state: TeacherHomeworkState) {
        binding.rvHomeworkList.submitList(state.homeworkList)
    }

    override fun updateEvent(event: TeacherHomeworkEvent) {
        when (event) {
            is TeacherHomeworkEvent.NavigateToAdd -> {
                navigationManager.navigate(NavCommand.ToDestination(R.id.teacherHomeworkEditFragment))
            }

            is TeacherHomeworkEvent.EditHomework -> {
                val bundle = Bundle().apply {
                    putInt("homeworkId", event.homeworkId)
                }
                navigationManager.navigate(
                    NavCommand.ToDestination(R.id.teacherHomeworkEditFragment, args = bundle)
                )
            }

            TeacherHomeworkEvent.NavigateBack -> navigationManager.navigate(NavCommand.Back)
        }
    }

}