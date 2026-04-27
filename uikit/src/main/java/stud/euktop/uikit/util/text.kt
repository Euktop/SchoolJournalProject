package stud.euktop.uikit.util

import android.widget.TextView

fun TextView.setTextUnique(text: String?) {
    if (this.text.equals(text)) return
    this.text = text ?: ""
}

fun TextView.setHintUnique(hint: String?) {
    if (this.hint.equals(hint)) return
    this.hint = hint ?: ""
}