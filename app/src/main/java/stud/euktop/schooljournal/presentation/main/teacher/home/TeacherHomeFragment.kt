package stud.euktop.schooljournal.presentation.main.teacher.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentTeacherHomeBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment

@AndroidEntryPoint
class TeacherHomeFragment : BaseFragment<
        FragmentTeacherHomeBinding,
        TeacherHomeViewModel,
        TeacherHomeState,
        Unit>() {

    override val viewModel: TeacherHomeViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentTeacherHomeBinding.inflate(inflater, container, false)

    override fun setupUI() {
        // TODO: Add UI setup
    }

    override fun updateState(state: TeacherHomeState) {
        // TODO: Update UI based on state
    }

    override fun updateEvent(event: Unit) {}
}

