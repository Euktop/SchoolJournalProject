package stud.euktop.schooljournal.presentation.auth.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.ActivitySplashBinding
import stud.euktop.schooljournal.presentation.common.navigate.contract.FrameNavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationRouterSplash
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    @Inject
    lateinit var routerSplash: NavigationRouterSplash

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ActivitySplashBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val start = System.currentTimeMillis()
        val navigate = routerSplash.navigationAfterSplash()
        lifecycleScope.launch {
            delay(
                maxOf(
                    resources.getInteger(R.integer.delay_loading)
                        .toLong() - System.currentTimeMillis() + start, 0
                )
            )
            navigate(requireActivity() as FrameNavigationManager)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}