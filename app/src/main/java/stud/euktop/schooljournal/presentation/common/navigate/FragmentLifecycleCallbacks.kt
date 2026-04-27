package stud.euktop.schooljournal.presentation.common.navigate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

class FragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
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
}