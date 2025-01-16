package org.connect.fitconnect.data.local

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.connect.fitconnect.core.CalendarUiState
import org.connect.fitconnect.core.WeekCalendarUiModel
import org.connect.fitconnect.utils.getDaysOfMonthStartingFromMonday

class MonthCalendarDataSource {
    fun getDates(month : Month, year : Int): List<CalendarUiState.Date> {
        return month.getDaysOfMonthStartingFromMonday(year)
            .map { date ->
                CalendarUiState.Date(
                    dayOfMonth = if (date.monthNumber == month.number) {
                        "${date.dayOfMonth}"
                    } else {
                        "" // Fill with empty string for days outside the current month
                    },
                    isSelected = date.dayOfYear == (Clock.System.todayIn(TimeZone.currentSystemDefault())).dayOfYear && date.monthNumber == month.number
                )
            }
    }
}

class WeekCalendarDataSource {
    val today: LocalDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): WeekCalendarUiModel {
        val firstDayOfWeek = startDate.previousMonday()
        val endDayOfWeek = firstDayOfWeek.plus(7, DateTimeUnit.DAY)
        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toUiModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = startDate.daysUntil(endDate)
        return List(numOfDays.toInt()) { index ->
            startDate.plus(index, DateTimeUnit.DAY)
        }
    }

    private fun toUiModel(
        dateList: List<LocalDate>,
        lastSelectedDate: LocalDate
    ): WeekCalendarUiModel {
        return WeekCalendarUiModel(
            selectedDate = toItemUiModel(lastSelectedDate, true),
            visibleDates = dateList.map { toItemUiModel(it, it == lastSelectedDate) }
        )
    }

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean) = WeekCalendarUiModel.Date(
        isSelected = isSelectedDate,
        isToday = date == today,
        date = date
    )

    // Extension function to get previous Monday
    private fun LocalDate.previousMonday(): LocalDate {
        var current = this
        while (current.dayOfWeek != DayOfWeek.MONDAY) {
            current = current.minus(1, DateTimeUnit.DAY)
        }
        return current
    }
}