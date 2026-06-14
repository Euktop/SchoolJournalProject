package stud.euktop.schooljournal.presentation.common.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import stud.euktop.schooljournal.R
import java.io.File
import java.net.URLConnection

object DownloadNotificationHelper {
    private const val CHANNEL_ID = "sj_downloads"
    private const val NOTIF_ID = 1001

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.download_notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    fun showDownloading(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(context.getString(R.string.download_notification_title))
            .setContentText(context.getString(R.string.download_notification_in_progress))
            .setProgress(0, 0, true)
            .setOngoing(true)
            .setAutoCancel(false)

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID, builder.build())
        } catch (_: SecurityException) {
        }
    }

    fun showDownloaded(context: Context, file: File) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.file_provider",
                file
            )
            val mime = URLConnection.guessContentTypeFromName(file.name) ?: "application/octet-stream"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mime)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val pending = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )

            val text = context.getString(R.string.download_saved_to, file.absolutePath)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle(context.getString(R.string.download_notification_done_title))
                .setContentText(text)
                .setContentIntent(pending)
                .setAutoCancel(true)
                .setOngoing(false)

            try {
                NotificationManagerCompat.from(context).notify(NOTIF_ID, builder.build())
            } catch (_: SecurityException) {
            }
        } catch (_: Throwable) {
            // ignore errors in notification helper
        }
    }

    fun cancel(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIF_ID)
    }
}

