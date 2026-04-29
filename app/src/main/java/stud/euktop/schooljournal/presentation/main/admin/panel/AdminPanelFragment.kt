package stud.euktop.schooljournal.presentation.main.admin.panel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminPanelBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment

//stud.euktop.schooljournal.presentation.main.admin.adminPanel.AdminPanelFragment
@AndroidEntryPoint
class AdminPanelFragment : BaseFragment<
        FragmentAdminPanelBinding,
        AdminPanelViewModel,
        AdminPanelState,
        Unit
        >() {
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentAdminPanelBinding.inflate(i, c, false)

    override val viewModel: AdminPanelViewModel by viewModels()

    override fun setupUI() {
        binding.viewPager.adapter = AdminPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = when (pos) {
                0 -> getString(R.string.users)
                1 -> getString(R.string.classes)
                2 -> getString(R.string.objects)
                3 -> getString(R.string.appointments)
                else -> ""
            }
        }.attach()
    }

    override fun updateState(state: AdminPanelState) {
        // TODO: Update state
    }

    override fun updateEvent(event: Unit) {}
}