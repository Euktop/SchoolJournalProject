package stud.euktop.schooljournal.presentation.common.delegate

import stud.euktop.schooljournal.presentation.common.base.BaseViewModel

interface ViewModelDelegate<VM : BaseViewModel<*, *>> {
    val viewModel: VM
}