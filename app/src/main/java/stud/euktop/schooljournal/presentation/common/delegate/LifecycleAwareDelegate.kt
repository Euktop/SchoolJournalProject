package stud.euktop.schooljournal.presentation.common.delegate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

interface LifecycleAwareDelegate {
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {}
    fun onViewCreated(lifecycleOwner: LifecycleOwner, savedInstanceState: Bundle?) {}
    fun onDestroyView() {}
    fun onDestroy() {}
}