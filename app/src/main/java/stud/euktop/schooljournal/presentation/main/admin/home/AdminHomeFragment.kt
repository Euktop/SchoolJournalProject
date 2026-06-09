package stud.euktop.schooljournal.presentation.main.admin.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentAdminHomeBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment

@AndroidEntryPoint
class AdminHomeFragment : BaseFragment<
        FragmentAdminHomeBinding,
        AdminHomeViewModel,
        AdminHomeState,
        Unit>() {

    override val viewModel: AdminHomeViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdminHomeBinding.inflate(inflater, container, false)

    override fun setupUI() {
        // TODO: Add UI setup
    }

    override fun updateState(state: AdminHomeState) {
        // TODO: Update UI based on state
    }

    override fun updateEvent(event: Unit) {}
}

