package net.decodex.invoice.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun Date.toLocaleDate(): LocalDate {
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
}