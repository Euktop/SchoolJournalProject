package stud.euktop.schooljournal.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.schooljournal.databinding.ActivityMainBinding
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigationManager: NavigationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    logger?.d(f.toSimpleTag(), "onFragmentCreated")
                }

                override fun onFragmentViewCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    v: View,
                    savedInstanceState: Bundle?
                ) {
                    logger?.d(f.toSimpleTag(), "onFragmentViewCreated")
                }

                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    logger?.d(f.toSimpleTag(), "onFragmentStarted")
                }

                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    logger?.d(f.toSimpleTag(), "onFragmentResumed")
                }

                override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                    logger?.d(f.toSimpleTag(), "onFragmentPaused")
                }

                override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                    logger?.d(f.toSimpleTag(), "onFragmentStopped")
                }

                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    logger?.d(f.toSimpleTag(), "onFragmentViewDestroyed")
                }

                override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                    logger?.d(f.toSimpleTag(), "onFragmentDestroyed")
                }
            },
            false
        )
        if (savedInstanceState == null) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(binding.fragmentContainer.id) as NavHostFragment
            val navController = navHostFragment.navController
            navigationManager.bindMain(navController)
        }
    }

}