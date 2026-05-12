package stud.euktop.data.map

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

internal fun LocalDateTime?.toDate(): Date =
    this?.let { local ->
        val instant = local.atZone(ZoneId.of("UTC")).toInstant()
        Date.from(instant)
    } ?: Date()

internal fun Date?.toLocalDateTime(): LocalDateTime =
    this?.let { date ->
        LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"))
    } ?: LocalDateTime.now(ZoneId.of("UTC"))