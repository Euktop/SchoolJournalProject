package stud.euktop.uikit.components.base

import android.os.Parcelable
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


abstract class SchJBase<STATE : Any> : SchJState<STATE>, ReadWriteProperty<Any?, STATE> {
    protected abstract fun initState(): STATE
    protected abstract fun updateState(state: STATE)
    protected abstract fun setupUI()
    protected var _state: STATE = initState()
    override var state: STATE
        get() = _state
        set(value) {
            if (_state == value) return
            updateState(value)
            _state = value
        }

    override fun getValue(thisRef: Any?, property: KProperty<*>): STATE {
        return _state
    }

    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: STATE
    ) {
        if (_state == value) return
        updateState(value)
        _state = value
    }

    protected open fun onCreate() {
        setupUI()
    }

    init {
        onCreate()
    }
}