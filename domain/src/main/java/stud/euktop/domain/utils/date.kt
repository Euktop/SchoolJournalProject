package stud.euktop.domain.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val baseDateFormat
    get() = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

fun String.toDate() = baseDateFormat.parse(this)!!
fun Date.toBaseString() = baseDateFormat.format(this)!!