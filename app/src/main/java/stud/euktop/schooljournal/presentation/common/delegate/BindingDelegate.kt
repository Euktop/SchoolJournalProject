package stud.euktop.schooljournal.presentation.common.delegate

import androidx.viewbinding.ViewBinding

interface BindingDelegate<BINDING : ViewBinding> {
    val binding: BINDING
}