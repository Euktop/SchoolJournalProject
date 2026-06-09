package stud.euktop.schooljournal.presentation.main.student.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentStudentHomeBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment

@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<
        FragmentStudentHomeBinding,
        StudentHomeViewModel,
        StudentHomeState,
        Unit>() {

    override val viewModel: StudentHomeViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStudentHomeBinding.inflate(inflater, container, false)

    override fun setupUI() {
        // TODO: Add UI setup
    }

    override fun updateState(state: StudentHomeState) {
        // TODO: Update UI based on state
    }

    override fun updateEvent(event: Unit) {}
}

