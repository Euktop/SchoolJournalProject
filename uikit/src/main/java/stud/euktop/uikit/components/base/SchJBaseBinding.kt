package stud.euktop.uikit.components.base

import android.os.Parcelable
import androidx.viewbinding.ViewBinding


internal abstract class SchJBaseBinding<BINDING : ViewBinding, STATE : Any> :
    SchJBase<STATE>() {
    lateinit var binding: BINDING
    protected abstract fun initBinding(): BINDING
    override fun onCreate() {
        binding = initBinding()
        super.onCreate()
    }
}