package stud.euktop.data.mock

import kotlinx.coroutines.delay

object MockDelayService {
    suspend fun delay() = delay(250)
}