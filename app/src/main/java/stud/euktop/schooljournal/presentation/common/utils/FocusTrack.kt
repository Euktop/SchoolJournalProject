package stud.euktop.schooljournal.presentation.common.utils

import android.view.View

class FocusTrack {
    private val _touchedFields = mutableSetOf<Int>()
    var onFocusChanged: ((Int?) -> Unit)? = null
    fun isTouched(viewId: Int?): Boolean = _touchedFields.contains(viewId)
    fun isTouched(view: View?): Boolean = isTouched(view?.id)
    fun markTouched(id: Int) {
        if (_touchedFields.add(id)) {
            onFocusChanged?.invoke(id)
        }
    }

    fun markTouched(view: View) = markTouched(view.id)

    fun clear() {
        _touchedFields.clear()
        onFocusChanged?.invoke(null)
    }

    fun removeFocus(viewId: Int) {
        _touchedFields.remove(viewId)
        onFocusChanged?.invoke(viewId)
    }

    fun removeFocus(view: View) = removeFocus(view.id)

    fun setFocusListener(view: View, id: Int = view.id, onFocusLost: (() -> Unit)? = null) {
        if (_touchedFields.contains(view.id)) return
        val f = view.onFocusChangeListener
        var isAfterVisible = false
        view.setOnFocusChangeListener { v, hasFocus ->
            f?.onFocusChange(v, hasFocus)
            if (isAfterVisible) return@setOnFocusChangeListener
            if (!hasFocus) {
                markTouched(id)
                onFocusLost?.invoke()
                isAfterVisible = true
            }
        }
    }
}