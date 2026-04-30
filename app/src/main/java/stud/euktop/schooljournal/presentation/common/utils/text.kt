package stud.euktop.schooljournal.presentation.common.utils

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat

fun getTextFromId(context: Context, id: Int?): String? {
    if (id == null) return null
    return ContextCompat.getString(context, id)
}

fun getTextFromId(resources: Resources, id: Int?, vararg formatArgs: Any): String? {
    if (id == null) return null
    return resources.getString(id, formatArgs)
}