package stud.euktop.schooljournal.presentation.common.binding

import android.widget.TextView
import stud.euktop.schooljournal.presentation.common.delegate.LoadingDelegate

fun TextView.bindLoading(
    loadingDelegate: LoadingDelegate<*>,
    key: String,
    loadingTextRes: Int? = null
) {
    var originalText: String? = null
    loadingDelegate.observeLoading(key) { isLoading ->
        isEnabled = !isLoading
        if (isLoading && loadingTextRes != null) {
            if (originalText == null) originalText = text.toString()
            text = context.getString(loadingTextRes)
        } else if (!isLoading && originalText != null) {
            text = originalText
        }
    }
}