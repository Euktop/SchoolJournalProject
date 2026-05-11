package stud.euktop.schooljournal.presentation.common.binding

import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate
import stud.euktop.uikit.databinding.LayoutButtonsSaveCancelBinding

fun LayoutButtonsSaveCancelBinding.toInit(
    loadingDelegate: LoadingDelegate<*>,
    save: () -> Unit,
    cancel: () -> Unit,
) {
    btnSave.bindLoading(loadingDelegate, "save", R.string.saving)
    btnSave.setOnClickListener { save() }
    btnCancel.setOnClickListener { cancel() }
}