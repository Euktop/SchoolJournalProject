package stud.euktop.data.map

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

internal fun LocalDateTime?.toDate(): Date =
    if (this == null) Date() else Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

internal fun Date?.toLocalDateTime(): LocalDateTime =
    (this ?: Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()