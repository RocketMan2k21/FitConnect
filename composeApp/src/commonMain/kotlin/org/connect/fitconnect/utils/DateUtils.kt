package org.connect.fitconnect.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getCurrentTime() = Clock.System.now()

fun areInstantsOnSameDay(instant1: Instant, instant2: Instant, timeZone: TimeZone): Boolean {
    val date1 = instant1.toLocalDateTime(timeZone).date
    val date2 = instant2.toLocalDateTime(timeZone).date
    return date1 == date2
}

fun String.parseToInstant(): Instant {
    val isoDateTime = this.replace(" ", "T")
    return Instant.parse(isoDateTime)
}