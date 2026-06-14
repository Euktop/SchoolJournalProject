package stud.euktop.schooljournal.presentation.main.teacher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentTeacherHomeBinding
import stud.euktop.schooljournal.presentation.common.base.BaseNavigationFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeFragment : BaseNavigationFragment<FragmentTeacherHomeBinding>(), stud.euktop.schooljournal.presentation.MainController {

    override val screenTag = "teacher_home"

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherHomeBinding.inflate(i, c, false)

    @Inject
    lateinit var router: RouterProfile

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.teacherNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navHostFragment.childFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fm: FragmentManager, fragment: Fragment) {
                    super.onFragmentResumed(fm, fragment)
                    if (fragment is DialogFragment) return
                    val config = (fragment as? ToolbarConfigProvider)?.getToolbarConfig()
                        ?: ToolbarConfig()
                    updateToolbar(config)
                }
            },
            false
        )
    }

    private fun updateToolbar(config: ToolbarConfig) {
        try {
            logger?.d(this.toSimpleTag(), "updateToolbar", "title=${config.titleRes?.let { getString(it) } ?: "default"}")
        } catch (_: Throwable) {
        }
        binding.toolbar.apply {
            title = config.titleRes?.let { getString(it) } ?: getString(R.string.app_name)
            menu.clear()
            inflateMenu(config.menuRes ?: R.menu.menu_home)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_profile -> router.toProfile()
                    else -> config.onMenuItemClick?.invoke(menuItem) ?: false
                }
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun switchToTab(menuItemId: Int) {
        try {
            binding.bottomNavigationView.selectedItemId = menuItemId
        } catch (_: Throwable) {
        }
    }
}
