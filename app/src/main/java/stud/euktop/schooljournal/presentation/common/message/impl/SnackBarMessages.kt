package stud.euktop.schooljournal.presentation.common.message.impl

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.message.contract.MessageDisplayer
import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam

class SnackBarMessages(
    private val view: View
) : MessageDisplayer {
    override fun message(param: MessageParam) {
        val text = ContextCompat.getString(view.context, param.message)
        Snackbar.make(
            view,
            text,
            view.context.resources.getInteger(R.integer.duration_notification)
        ).apply { }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                when (event) {
                    DISMISS_EVENT_ACTION,
                    DISMISS_EVENT_MANUAL,
                    DISMISS_EVENT_CONSECUTIVE,
                    DISMISS_EVENT_TIMEOUT -> param.action?.invoke()

                    DISMISS_EVENT_SWIPE -> if (param.dismiss == null) param.action?.invoke() else param.dismiss()
                }
                super.onDismissed(transientBottomBar, event)
            }
        }).show()
    }
}