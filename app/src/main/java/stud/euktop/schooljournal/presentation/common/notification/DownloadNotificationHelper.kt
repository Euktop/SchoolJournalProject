package stud.euktop.schooljournal.presentation.common.notification

import android.content.Context
import java.io.File
import stud.euktop.schooljournal.presentation.common.base.DownloadNotificationHelper as BaseDownloadNotificationHelper

object DownloadNotificationHelper {
    fun createChannel(context: Context) {
        BaseDownloadNotificationHelper.createChannel(context)
    }

    fun showDownloading(context: Context) {
        BaseDownloadNotificationHelper.showDownloading(context)
    }

    fun showDownloaded(context: Context, file: File) {
        BaseDownloadNotificationHelper.showDownloaded(context, file)
    }

    fun cancel(context: Context) {
        BaseDownloadNotificationHelper.cancel(context)
    }
}

