package stud.euktop.schooljournal.presentation.common.utils

import android.view.View

class FocusTrack {
    private val _touchedFields = mutableSetOf<Int>()
    var onFocusChanged: ((Int?) -> Unit)? = null
    fun isTouched(view: View?): Boolean = _touchedFields.contains(view?.id)
    fun markTouched(id: Int) {
        if (_touchedFields.add(id)) {
            onFocusChanged?.invoke(id)
        }
    }

    fun clear() {
        _touchedFields.clear()
        onFocusChanged?.invoke(null)
    }

    fun removeFocus(view: View) {
        _touchedFields.remove(view.id)
        onFocusChanged?.invoke(view.id)
    }

    fun setFocusListener(view: View, onFocusLost: (() -> Unit)? = null) {
        if (_touchedFields.contains(view.id)) return
        view.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                markTouched(view.id)
                onFocusLost?.invoke()
                view.onFocusChangeListener = null
            }
        }
    }

}