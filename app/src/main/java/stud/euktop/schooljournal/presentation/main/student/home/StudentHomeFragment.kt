package stud.euktop.schooljournal.presentation.main.student.home

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
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.FragmentStudentHomeBinding
import stud.euktop.schooljournal.presentation.common.base.BaseNavigationFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfig
import stud.euktop.schooljournal.presentation.common.toolbar.ToolbarConfigProvider
import javax.inject.Inject
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

@AndroidEntryPoint
class StudentHomeFragment : BaseNavigationFragment<FragmentStudentHomeBinding>(), stud.euktop.schooljournal.presentation.MainController {

    override val screenTag = "student_home"

    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentStudentHomeBinding.inflate(i, c, false)

    @Inject
    lateinit var router: RouterProfile

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.studentNavHostFragment) as NavHostFragment
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

    // Быстрый safe-путь переключения таба: фрагменты внутри StudentHomeFragment вызывают
    // (parentFragment as? MainController)?.switchToTab(R.id.some) и BottomNavigationView
    // выполнит навигацию через setupWithNavController.
    override fun switchToTab(menuItemId: Int) {
        try {
            binding.bottomNavigationView.selectedItemId = menuItemId
        } catch (_: Throwable) {
        }
    }
}