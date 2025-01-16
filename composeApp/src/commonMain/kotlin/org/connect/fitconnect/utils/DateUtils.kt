package org.connect.fitconnect.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

fun getCurrentTime() = Clock.System.now()

fun areInstantsOnSameDay(instant1: Instant, instant2: Instant, timeZone: TimeZone): Boolean {
    val date1 = instant1.toLocalDateTime(timeZone).date
    val date2 = instant2.toLocalDateTime(timeZone).date
    return date1 == date2
}

fun String.parseToInstant(): Instant {
    val isoFormatString = this.replace(" ", "T") + "Z"
    return Instant.parse(isoFormatString)
}

fun Month.getDaysOfMonthStartingFromMonday(year: Int): List<LocalDate> {
    val firstDayOfMonth = LocalDate(year, this, 1)

    // Find the first Monday of the month
    val daysUntilMonday = (DayOfWeek.MONDAY.ordinal - firstDayOfMonth.dayOfWeek.ordinal + 7) % 7
    val firstMondayOfMonth = firstDayOfMonth.plus(daysUntilMonday, kotlinx.datetime.DateTimeUnit.DAY)

    // Calculate the first day of the next month
    val firstDayOfNextMonth = if (this.ordinal == 11) { // If December
        LocalDate(year + 1, kotlinx.datetime.Month.JANUARY, 1)
    } else {
        LocalDate(year, this.ordinal + 1, 1)
    }

    // Generate all Mondays within the current month
    return generateSequence(firstMondayOfMonth) { it.plus(7, kotlinx.datetime.DateTimeUnit.DAY) }
        .takeWhile { it < firstDayOfNextMonth }
        .toList()
}

fun Month.getDisplayName(year : Int): String {
    return "${this.name} $year"
}

val daysOfWeek: Array<String>
    get() {
        val daysOfWeek = Array(7) { "" }

        for (dayOfWeek in DayOfWeek.entries) {
            val localizedDayName = dayOfWeek.name
            daysOfWeek[dayOfWeek.isoDayNumber] = localizedDayName
        }

        return daysOfWeek
    }

fun getYearFromMonth(month: Month, day: Int = 1): Int {
    // Use a default day if not provided
    val date = LocalDate(year = 2024, month = month, dayOfMonth = day) // Replace with your desired year
    return date.year
}

fun getDayAbbreviation(date: LocalDate): String {
    val dayOfWeekToAbbreviation = mapOf(
        DayOfWeek.MONDAY to "Mon",
        DayOfWeek.TUESDAY to "Tue",
        DayOfWeek.WEDNESDAY to "Wed",
        DayOfWeek.THURSDAY to "Thu",
        DayOfWeek.FRIDAY to "Fri",
        DayOfWeek.SATURDAY to "Sat",
        DayOfWeek.SUNDAY to "Sun"
    )
    return dayOfWeekToAbbreviation[date.dayOfWeek] ?: "Unknown"
}

object DateUtils {


    fun parseDateFromDatabase(date : String): LocalDateTime {
        val newDate = date.replace(" ", "T")
        return LocalDateTime.parse(newDate)
    }
}

