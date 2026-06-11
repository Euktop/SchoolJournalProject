package stud.euktop.schooljournal.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import javax.inject.Inject

abstract class BaseNavigationFragment<BINDING : ViewBinding> : Fragment() {

    protected lateinit var binding: BINDING

    @Inject
    lateinit var manager: NavigationManager

    protected abstract val screenTag: String
    protected abstract fun inflateBinding(i: LayoutInflater, c: ViewGroup?): BINDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateBinding(inflater, container)
        return binding.root
    }

    protected open fun getNavController(): NavController = findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = getNavController()
        manager.register(screenTag, navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        manager.unregister(screenTag)
    }

}