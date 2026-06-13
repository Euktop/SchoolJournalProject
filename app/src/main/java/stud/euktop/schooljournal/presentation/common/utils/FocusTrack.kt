package stud.euktop.schooljournal.presentation.common.utils

import android.view.View
import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

class FocusTrack {
    private val _touchedFields = mutableSetOf<Int>()
    var onFocusChanged: ((Int?) -> Unit)? = null
    fun isTouched(viewId: Int?): Boolean = _touchedFields.contains(viewId)
    fun isTouched(view: View?): Boolean = isTouched(view?.id)
    fun markTouched(id: Int) {
        if (_touchedFields.add(id)) {
            try {
                logger?.d(this.toSimpleTag(), "focusMarked", "id=$id")
            } catch (_: Throwable) {
            }
            onFocusChanged?.invoke(id)
        }
    }

    fun markTouched(view: View) = markTouched(view.id)

    fun clear() {
        _touchedFields.clear()
        try {
            logger?.d(this.toSimpleTag(), "focusCleared")
        } catch (_: Throwable) {
        }
        onFocusChanged?.invoke(null)
    }

    fun removeFocus(viewId: Int) {
        _touchedFields.remove(viewId)
        try {
            logger?.d(this.toSimpleTag(), "focusRemoved", "id=$viewId")
        } catch (_: Throwable) {
        }
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