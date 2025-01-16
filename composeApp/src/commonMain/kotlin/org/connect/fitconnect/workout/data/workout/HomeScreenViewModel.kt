package org.connect.fitconnect.workout.data.workout

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.core.WeekCalendarUiModel
import org.connect.fitconnect.domain.ExerciseSet
import org.connect.fitconnect.domain.workout.Workout
import org.connect.fitconnect.utils.areInstantsOnSameDay
import org.connect.fitconnect.utils.getCurrentTime
import org.connect.fitconnect.utils.parseToInstant

class HomeScreenViewModel(
    private val useCases: HomeScreenUseCases
) : ScreenModel {

    private var _allWorkouts: MutableStateFlow<WorkoutListState> =
        MutableStateFlow(WorkoutListState.Idle)
    val allWorkouts: StateFlow<WorkoutListState> = _allWorkouts.asStateFlow()

    private var _currentWorkoutId: MutableStateFlow<Long> = MutableStateFlow(-1)
        val currentWorkoutId: StateFlow<Long> = _currentWorkoutId.asStateFlow()

    private var _currentWorkout: MutableStateFlow<UiState<List<ExerciseSet>>> =
        MutableStateFlow(UiState.Idle)
    val currentWorkout: StateFlow<UiState<List<ExerciseSet>>> = _currentWorkout.asStateFlow()

    init {
        fetchWorkouts()
        // To know the current workouts amount
        // User sees one screen with workout data. On first load - shows today's day workout
    }

    // Loads workout data for specific date
    private fun fetchWorkoutForTheDate(currentDate: Instant) {
        screenModelScope.launch {
            if (allWorkouts.value is WorkoutListState.Success) {
                val workout =
                    (allWorkouts.value as WorkoutListState.Success).data.filter { workout: Workout ->
                        val workoutInstantTime = workout.date.parseToInstant()
                        areInstantsOnSameDay(currentDate, workoutInstantTime, TimeZone.UTC)
                    }

                if (workout.isNotEmpty()) {
                    _currentWorkoutId.emit(workout.first().id)
                    loadWorkoutData()
                } else {
                    _currentWorkoutId.emit(-1)
                    _currentWorkout.emit(UiState.Success(emptyList()))
                }
            }
        }
    }

    private suspend fun loadWorkoutData() {
        _currentWorkoutId.collect {
            useCases.getSetsForWorkout.invoke(it.toInt()).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        _currentWorkout.emit(UiState.Error("Couldn't load workout data, please try again later"))
                    }

                    is Result.Success -> {
                        _currentWorkout.emit(UiState.Success(result.data))
                    }
                }
            }
        }
    }

    private fun fetchWorkouts() {
        screenModelScope.launch {
            useCases.getAllWorkoutsUseCase.invoke().collectLatest { resource ->
                when (resource) {
                    is Result.Error -> {
                        _allWorkouts.emit(WorkoutListState.Error("Error while fetching workout data, please try again later"))
                    }

                    is Result.Success -> {
                        _allWorkouts.emit(WorkoutListState.Success(resource.data))
                        fetchWorkoutForTheDate(getCurrentTime())
                    }
                }
            }
        }
    }

    fun onAddWorkoutClick() {
        initiateNewWorkout()
    }

    private fun initiateNewWorkout() {
        screenModelScope.launch {
            when (val resource = useCases.initiateNewWorkout.invoke()) {
                is Result.Error -> {
                    _currentWorkout.emit(UiState.Error("Couldn't initiate new workout, please try again later"))
                }

                is Result.Success -> {
                    _currentWorkoutId.update { resource.data.id  }
                    // Goes to exercise selection screen
                }
            }
        }
    }

    fun onDateSelectedClick(newDate : WeekCalendarUiModel.Date) {
        fetchWorkoutForTheDate(newDate.date.atTime(12, 30).toInstant(TimeZone.currentSystemDefault()))
    }

    sealed class WorkoutListState {
        data object Idle : WorkoutListState()
        data class Success(val data: List<Workout>) : WorkoutListState()
        data class Error(val error: String) : WorkoutListState()
    }

}