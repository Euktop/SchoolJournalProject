package stud.euktop.schooljournal.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.ActivityMainBinding
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam
import stud.euktop.schooljournal.presentation.common.message.impl.SnackBarMessages
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.RegisterFragmentLifecycleCallbacks
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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

        if (savedInstanceState == null) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(binding.fragmentContainer.id) as NavHostFragment
            navController = navHostFragment.navController
            navigationManager.bindMain(navController)
            navHostFragment.childFragmentManager.registerFragmentLifecycleCallbacks(
                RegisterFragmentLifecycleCallbacks,
                false
            )
        } else {
            navController = supportFragmentManager
                .findFragmentById(binding.fragmentContainer.id)!!
                .let { (it as NavHostFragment).navController }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isDismissed)
                {
                    finish()
                    return
                }
                if (navController.previousBackStackEntry == null) {
                    val activity = WeakReference(this@MainActivity)
                    isDismissed = true
                    SnackBarMessages(binding.root,lifecycleScope).message(
                        MessageParam(
                            R.string.press_again_to_exit,
                            action = {
                                activity.get()?.isDismissed = false
                            }
                        )
                    )
                } else {
                    navigationManager.navigate(NavCommand.Back)
                }
            }
        })
    }

    private var isDismissed = false
}