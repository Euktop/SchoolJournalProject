package stud.euktop.schooljournal.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
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
        try {
            logger?.d(this.toSimpleTag(), "onCreate", "activity created")
        } catch (_: Throwable) {
        }
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
            try {
                logger?.d(this.toSimpleTag(), "onCreate", "navigation initialized from savedInstanceState=null")
            } catch (_: Throwable) {
            }
        } else {
            navController = supportFragmentManager
                .findFragmentById(binding.fragmentContainer.id)!!
                .let { (it as NavHostFragment).navController }
            try {
                logger?.d(this.toSimpleTag(), "onCreate", "navigation restored from savedInstanceState")
            } catch (_: Throwable) {
            }
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
                    try {
                        logger?.d(this@MainActivity.toSimpleTag(), "handleOnBackPressed", "showing exit confirmation")
                    } catch (_: Throwable) {
                    }
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