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
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.ActivitySplashBinding
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import javax.inject.Inject
/**
 * Сплэш-экран, отображаемый при запуске приложения.
 *
 * Назначение: показать заставку с логотипом и названием,
 * выполнить минимальную задержку (из ресурсов), затем перейти к основному экрану.
 *
 * Роли: любой пользователь (неавторизованный или авторизованный)
 *
 * Функционал:
 * - Отображение статического контента (изображение + текст)
 * - Задержка на время, указанное в integers.xml (delay_loading)
 * - Перенаправление через RouterSplash.navigateAction()
 *
 * @see RouterSplash
 */
@AndroidEntryPoint
class SplashFragment : Fragment() {
    @Inject
    lateinit var routerSplash: RouterSplash

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ActivitySplashBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val start = System.currentTimeMillis()
        lifecycleScope.launch {
            delay(
                maxOf(
                    resources.getInteger(R.integer.delay_loading)
                        .toLong() - System.currentTimeMillis() + start, 0
                )
            )
            routerSplash.navigateAction()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}