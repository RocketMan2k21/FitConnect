package org.connect.fitconnect.workout.data.history

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.SetGroupedByWorkoutDate
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.domain.workout.SetRepository
import org.connect.fitconnect.domain.workout.Workout
import org.connect.fitconnect.domain.workout.WorkoutRepository
import org.connect.fitconnect.utils.parseToInstant

class SetHistoryViewModel(
    private val setRepository: SetRepository,
) : ScreenModel {

    private var _setHistoryList : MutableStateFlow<UiState<List<SetGroupedByWorkoutDate>>> = MutableStateFlow(UiState.Idle)
    val setHistoryList : StateFlow<UiState<List<SetGroupedByWorkoutDate>>> = _setHistoryList.asStateFlow()

    fun getAllSetByExerciseId(exerciseId : Int) {
        screenModelScope.launch {
            setRepository.getAllSetsGroupedByDate().collectLatest {
                when (it) {
                    is Result.Error -> {
                        _setHistoryList.emit(UiState.Error("Couldn't fetch set history data, please try again"))
                    }
                    is Result.Success -> {
                        val workoutSetExerciseUiState = filterByExerciseId(it.data, exerciseId)
                        _setHistoryList.emit(UiState.Success(workoutSetExerciseUiState))
                    }
                }
            }
        }
    }

    private fun filterByExerciseId(
        data: List<SetGroupedByWorkoutDate>,
        exerciseId: Int
    ): List<SetGroupedByWorkoutDate> {
        return data.filter { it.exercise.id.toInt() == exerciseId }
    }
}