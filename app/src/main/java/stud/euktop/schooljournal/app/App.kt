package stud.euktop.schooljournal.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        stud.euktop.domain.utils.loger.logger = object : stud.euktop.domain.utils.loger.Logger {
            private inline fun message(
                tag: String,
                action: String,
                data: String?,
                block: (Timber.Tree, String) -> Unit
            ) = block(
                Timber.tag(tag),
                if (data.isNullOrEmpty()) action else "$action --- $data"
            )

            override fun i(tag: String, action: String, data: String?) {
                message(tag, action, data) { a, i -> a.i(i) }
            }

            override fun d(tag: String, action: String, data: String?) {
                message(tag, action, data) { a, i -> a.d(i) }
            }

            override fun e(
                tag: String,
                action: String,
                throwable: Throwable?,
                data: String?
            ) {
                message(tag, action, data) { a, i -> a.e(throwable, i) }
            }

        }
    }
}