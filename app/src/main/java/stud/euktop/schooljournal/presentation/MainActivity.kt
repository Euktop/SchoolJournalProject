package stud.euktop.schooljournal.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.ActivityMainBinding
import stud.euktop.schooljournal.presentation.common.navigate.Destination
import stud.euktop.schooljournal.presentation.common.navigate.FragmentLifecycleCallbacks
import stud.euktop.schooljournal.presentation.common.navigate.StackAction
import stud.euktop.schooljournal.presentation.common.navigate.contract.FrameNavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.impl.NavigationManagerImpl

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FrameNavigationManager {
    private lateinit var binding: ActivityMainBinding
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

        navigationManager =
            NavigationManagerImpl(supportFragmentManager, binding.fragmentContainer.id)
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            FragmentLifecycleCallbacks(),
            false
        )
        if (savedInstanceState == null)
            navigationManager.navigateTo(Destination.Splash, StackAction.CLEAR_ALL)
    }

    override lateinit var navigationManager: NavigationManager
}