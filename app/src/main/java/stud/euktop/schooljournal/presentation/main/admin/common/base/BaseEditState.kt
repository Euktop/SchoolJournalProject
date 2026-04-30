package stud.euktop.schooljournal.presentation.main.admin.common.base

import stud.euktop.schooljournal.presentation.common.base.BaseState

abstract class BaseEditState<T : BaseState<T>> : BaseState<T>() {
    abstract fun isEditMode(): Boolean
    abstract fun isFormValid(): Boolean
}