package stud.euktop.schooljournal.presentation.main.teacher.teacherClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherClassesBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.main.teacher.teacherLessons.TeacherLessonsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TeacherClassesFragment : BaseFragment<
        FragmentTeacherClassesBinding,
        TeacherClassesViewModel,
        TeacherClassesState,
        Unit
        >() {
    @Inject
    internal lateinit var navigationManager: NavigationManager
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherClassesBinding.inflate(i, c, false)

    override val viewModel: TeacherClassesViewModel by viewModels()
    private var adapter: TeacherClassAdapter? = null

    override fun setupUI() {
        adapter = TeacherClassAdapter { item ->
            navigationManager.navigate(
                NavCommand.ToDestination(
                    destId = R.id.teacherLessonsFragment,
                    args = Bundle().apply {
                        putInt(TeacherLessonsViewModel.CLASS_ID, item.classId)
                        putInt(TeacherLessonsViewModel.SUBJECT_ID, item.subjectId)
                    }
                )
            )
        }
        binding.rvClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvClasses.adapter = adapter
        viewModel.loadClasses()
    }

    override fun updateState(state: TeacherClassesState) {
        adapter?.submitList(state.classes)
    }

    override fun updateEvent(event: Unit) {}
}