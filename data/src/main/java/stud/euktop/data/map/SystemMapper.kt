package stud.euktop.data.map

import com.schooljournal.model.GetSystemInfoResult

internal fun GetSystemInfoResult.toAboutInfo(): String {
    return "School Journal v${this.version ?: "1.0"} (${this.environment ?: "Production"})"
}