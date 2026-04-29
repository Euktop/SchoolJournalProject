package stud.euktop.uikit.util

import android.widget.TextView

fun TextView.setTextUnique(text: String?) {
    if (this.text?.toString() == text) return
    this.text = text ?: ""
}

fun TextView.setHintUnique(hint: String?) {
    if (this.hint?.toString() == hint) return
    this.hint = hint ?: ""
}