package org.connect.fitconnect.calendar

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import org.connect.fitconnect.core.CalendarUiState
import org.connect.fitconnect.data.local.MonthCalendarDataSource
import org.connect.fitconnect.utils.getYearFromMonth

//class HomeCalendarViewModel : ScreenModel {
//
//    private val dataSource by lazy { MonthCalendarDataSource() }
//
//    private val _uiState = MutableStateFlow(CalendarUiState.Init)
//    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
//
//    init {
//        screenModelScope.launch {
//            _uiState.update { currentState ->
//                currentState.copy(
//                    dates = dataSource.getDates(currentState.month)
//                )
//            }
//        }
//    }
//
//
//    fun toNextMonth(nextMonth: Month) {
//        screenModelScope.launch {
//            _uiState.update { currentState ->
//                currentState.copy(
//                    month = nextMonth,
//                    year = getYearFromMonth(month = nextMonth),
//                    dates = dataSource.getDates(nextMonth)
//                )
//            }
//        }
//    }
//
//    fun toPreviousMonth(prevMonth: Month) {
//        screenModelScope.launch {
//            _uiState.update { currentState ->
//                currentState.copy(
//                    month = prevMonth,
//                    year = getYearFromMonth(month = prevMonth),
//                    dates = dataSource.getDates(prevMonth)
//                )
//            }
//        }
//    }
//}