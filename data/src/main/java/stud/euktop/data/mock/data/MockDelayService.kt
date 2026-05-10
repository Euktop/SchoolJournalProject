package stud.euktop.data.mock.data

import kotlinx.coroutines.delay

internal object MockDelayService {
    suspend fun delay() = delay(250)
}