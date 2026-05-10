package stud.euktop.schooljournal.presentation.common.delegate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

class BindingDelegate<BINDING : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> BINDING
) : LifecycleAwareDelegate {

    private var _binding: BINDING? = null
    val binding: BINDING
        get() = _binding ?: error("Binding accessed before onCreateView or after onDestroyView")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        _binding = inflate(inflater, container, false)
    }

    override fun onDestroyView() {
        _binding = null
    }
}