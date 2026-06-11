package stud.euktop.schooljournal.presentation.main.admin.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.NavMainMainDirections
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentAdminHomeBinding
import stud.euktop.schooljournal.presentation.common.base.BaseNavigationFragment
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import stud.euktop.schooljournal.presentation.common.utils.observeState

@AndroidEntryPoint
class AdminHomeFragment : BaseNavigationFragment<FragmentAdminHomeBinding>() {

    override val screenTag = "admin_home"

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentAdminHomeBinding.inflate(i, c, false)

    private val viewModel: AdminHomeViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var navigationView: com.google.android.material.navigation.NavigationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.adminNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        navigationView = binding.drawerAdmin.navigationView
        navigationView.setCheckedItem(R.id.nav_dashboard)

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> viewModel.onDashboardClick()
                R.id.nav_schools -> viewModel.onSchoolsClick()
                R.id.nav_classes -> viewModel.onClassesClick()
                R.id.nav_subjects -> viewModel.onSubjectsClick()
                R.id.nav_rooms -> viewModel.onRoomsClick()
                R.id.nav_assignments -> viewModel.onAssignmentsClick()
                R.id.nav_users -> viewModel.onUsersClick()
                R.id.nav_audit -> viewModel.onAuditClick()
                R.id.nav_settings -> viewModel.onSettingsClick()
            }
            navigationView.setCheckedItem(menuItem.itemId)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.adminDashboardFragment -> navigationView.setCheckedItem(R.id.nav_dashboard)
                R.id.schoolsListFragment -> navigationView.setCheckedItem(R.id.nav_schools)
                R.id.classesListFragment -> navigationView.setCheckedItem(R.id.nav_classes)
                R.id.subjectsListFragment -> navigationView.setCheckedItem(R.id.nav_subjects)
                R.id.roomsListFragment -> navigationView.setCheckedItem(R.id.nav_rooms)
                R.id.assignmentsListFragment -> navigationView.setCheckedItem(R.id.nav_assignments)
                R.id.usersListFragment -> navigationView.setCheckedItem(R.id.nav_users)
                R.id.auditLogFragment -> navigationView.setCheckedItem(R.id.nav_audit)
                R.id.settingsFragment -> navigationView.setCheckedItem(R.id.nav_settings)
            }
        }

        observeState(viewModel.state) { state ->
            val headerView = navigationView.getHeaderView(0)
            headerView.findViewById<TextView>(R.id.tvDrawerUserName).text = state.adminName
            headerView.findViewById<TextView>(R.id.tvDrawerUserEmail).text = state.adminEmail
        }

        navHostFragment.childFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fm: FragmentManager, nextFragment: Fragment) {
                    super.onFragmentResumed(fm, nextFragment)
                    if (nextFragment is DialogFragment) return
                    val config = if (nextFragment is ToolbarConfigProvider) {
                        nextFragment.getToolbarConfig()
                    } else {
                        ToolbarConfig(titleRes = R.string.app_name)
                    }
                    updateToolbar(config)
                }
            }, false
        )
    }

    private fun updateToolbar(config: ToolbarConfig) {
        binding.toolbar.apply {
            title = config.titleRes?.let { getString(it) } ?: getString(R.string.app_name)
            menu.clear()
            config.menuRes?.let { inflateMenu(it) }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_profile -> {
                        findNavController().navigate(NavMainMainDirections.actionGlobalProfile())
                        true
                    }

                    else -> {
                        config.onMenuItemClick?.invoke(menuItem)
                        false
                    }
                }
            }
        }
    }

    override fun getNavController(): NavController {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.adminNavHostFragment) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}