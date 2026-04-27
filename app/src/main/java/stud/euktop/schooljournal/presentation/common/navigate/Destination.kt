package stud.euktop.schooljournal.presentation.common.navigate

import androidx.fragment.app.Fragment
import stud.euktop.schooljournal.presentation.auth.login.LoginFragment
import stud.euktop.schooljournal.presentation.auth.splash.SplashFragment

sealed class Destination(val id: String, val group: Group, val action: () -> Fragment) {
    object Splash : Destination("splash", Group.AUTH, { SplashFragment() })
    object Login : Destination("login", Group.AUTH, { LoginFragment() })
}