package org.connect.fitconnect.core

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DateTimeFormatBuilder
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.todayIn
import org.connect.fitconnect.utils.getDayAbbreviation


data class CalendarUiState(
    val week : Int,
    val month: Month,
    val year : Int,
    val dates: List<Date>
) {
    companion object {
        val Init = CalendarUiState(
            week = getCurrentWeekNumber(),
            month = Clock.System.todayIn(TimeZone.currentSystemDefault()).month,
            year = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
            dates = emptyList()
        )

        private fun getCurrentWeekNumber(): Int {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            return today.monthNumber*(31/7)
        }
    }
    data class Date(
        val dayOfMonth: String,
        val isSelected: Boolean
    ) {
        companion object {
            val Empty = Date("", false)
        }
    }
}

data class WeekCalendarUiModel(
    val selectedDate: Date, // the date selected by the User. by default is Today.
    val visibleDates: List<Date> // the dates shown on the screen
) {

    val startDate: Date = visibleDates.first() // the first of the visible dates
    val endDate: Date = visibleDates.last() // the last of the visible dates

    data class Date(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean
    ) {
        @OptIn(FormatStringsInDatetimeFormats::class)
        val day: String = getDayAbbreviation(date)
    }
}
