package stud.euktop.schooljournal.presentation.main.student.studentSubjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentStudentSubjectsBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

//stud.euktop.schooljournal.presentation.main.student.studentSubjects.StudentSubjectsFragment
@AndroidEntryPoint
class StudentSubjectsFragment : BaseFragment<
        FragmentStudentSubjectsBinding,
        StudentSubjectsViewModel,
        StudentSubjectsState,
        Unit
        >() {
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentStudentSubjectsBinding.inflate(i, c, false)

    override val viewModel: StudentSubjectsViewModel by viewModels()

    @Inject
    internal lateinit var navigationManager: NavigationManager

    override fun setupUI() {
        val adapter = StudentSubjectAdapter { item ->
            navigationManager.navigate(
                NavCommand.ToDestination(
                    destId = R.id.studentSubjectDetailFragment,
                    args = Bundle().apply {
                        putInt("subjectId", item.subjectId)
                    }
                )
            )
        }
        binding.rvSubjects.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSubjects.adapter = adapter
    }

    override fun updateState(state: StudentSubjectsState) {
        (binding.rvSubjects.adapter as? StudentSubjectAdapter)?.submitList(state.subjects)
    }

    override fun updateEvent(event: Unit) {}
}