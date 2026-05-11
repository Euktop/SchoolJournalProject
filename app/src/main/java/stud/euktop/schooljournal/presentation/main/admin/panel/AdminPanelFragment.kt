package stud.euktop.schooljournal.presentation.main.admin.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminPanelBinding
import stud.euktop.schooljournal.presentation.main.admin.assignments.AssignmentsListFragment
import stud.euktop.schooljournal.presentation.main.admin.classes.ClassesListFragment
import stud.euktop.schooljournal.presentation.main.admin.subjects.SubjectsListFragment
import stud.euktop.schooljournal.presentation.main.admin.users.UsersListFragment

@AndroidEntryPoint
class AdminPanelFragment : Fragment() {

    private var _binding: FragmentAdminPanelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 4
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> UsersListFragment()
                1 -> ClassesListFragment()
                2 -> SubjectsListFragment()
                3 -> AssignmentsListFragment()
                else -> throw IllegalArgumentException()
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.users)
                1 -> getString(R.string.classes)
                2 -> getString(R.string.subjects)
                3 -> getString(R.string.assignments)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}