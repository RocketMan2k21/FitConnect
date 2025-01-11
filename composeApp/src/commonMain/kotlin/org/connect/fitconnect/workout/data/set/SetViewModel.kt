package org.connect.fitconnect.workout.data.set

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.domain.workout.SetRepository

class SetViewModel(
    private val setRepository: SetRepository
) : ScreenModel {

    private var _currentSetList : MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val currentSetList : StateFlow<UiState> = _currentSetList.asStateFlow()

    var currentSet: MutableState<Set?> = mutableStateOf(null)
        private set

    private var _uiState : MutableStateFlow<SetUiState> = MutableStateFlow(SetUiState())
    val uiState : StateFlow<SetUiState> = _uiState.asStateFlow()

    private var _buttonsUiState : MutableStateFlow<ButtonsUiState> = MutableStateFlow(ButtonsUiState())
    val buttonsUiState : StateFlow<ButtonsUiState> = _buttonsUiState.asStateFlow()

    private var _currentWeight : MutableSharedFlow<Double> = MutableSharedFlow(0)
    val currentWeight : SharedFlow<Double> = _currentWeight

    private var _currentReps : MutableSharedFlow<Int> = MutableSharedFlow(0)
    val currentReps : SharedFlow<Int> = _currentReps

    fun fetchSetsForExercise(exerciseId : Int, workoutId : Int) {
        screenModelScope.launch {
            _currentSetList.emit(UiState.Loading)

            setRepository.getAllSetsForExerciseAndWorkout(workoutId, exerciseId)
                .collectLatest { resource ->
                    when(resource) {
                        is Result.Error -> {
                            _currentSetList.emit(UiState.Error("Couldn't load set data, please try again later"))
                        }
                        is Result.Success -> {
                            _currentSetList.emit(UiState.Success(resource.data))
                        }
                    }
                }
        }
    }

    fun onSaveSetClick(workoutId: Int, exerciseId: Int) {
        val newSet = Set(
            id = 0,
            exercise_id = exerciseId,
            workout_id = workoutId,
            reps = uiState.value.reps,
            weight = uiState.value.weight
        )
        screenModelScope.launch {
            val result = setRepository.insertNewSet(newSet).first()

            when(result) {
                is Result.Error -> {
                    _currentSetList.emit(UiState.Error("Couldn't not add new set, please try again later"))
                }
                is Result.Success -> {
                    val insertedSet = result.data
                   _uiState.emit(
                       SetUiState(
                           weight = insertedSet.weight ?: 0.0,
                           reps = insertedSet.reps
                       )
                   )
                }
            }
        }
    }

    fun onUpdateClick(workoutId: Int, exerciseId: Int) {
        val newSet = Set(
            id = 0,
            exercise_id = exerciseId,
            workout_id = workoutId,
            reps = uiState.value.reps,
            weight = uiState.value.weight
        )

        screenModelScope.launch {
            val result = setRepository.updateSet(newSet).first()

            when(result) {
                is Result.Error -> {
                    _currentSetList.emit(UiState.Error("Couldn't not add new set, please try again later"))
                }
                is Result.Success -> {
                    val insertedSet = result.data
                    _uiState.emit(
                        SetUiState(
                            weight = insertedSet.weight ?: 0.0,
                            reps = insertedSet.reps
                        )
                    )
                }
            }
        }
    }

    fun onDeleteClick() {
        screenModelScope.launch {
            currentSet.value?.let {
                val result = setRepository.deleteSet(it.id.toInt()).first()

                when(result) {
                    is Result.Error -> {
                       println("Error while deleting the set")
                    }
                    is Result.Success -> {
                        println("Set ${it.id} was deleted successfully")
                    }
                }
            }
        }
    }

    fun onSetClick() {
        screenModelScope.launch {
            currentSet.value?.let {
                updateWeightAndRepsState(it.weight ?: 0.0, it.reps)
                switchButtons()
            }
        }
    }

    fun onClearClick() {
        screenModelScope.launch {
            updateWeightAndRepsState(0.0, 0)
        }
    }

    private suspend fun switchButtons() {
        _buttonsUiState.emit(
            ButtonsUiState(
                primaryButtonState = PrimaryButtonState.UPDATE,
                secondaryButtonState = SecondaryButtonState.DELETE,
            )
        )
    }

    private suspend fun updateWeightAndRepsState(weight: Double, reps: Int) {
        _uiState.emit(
            SetUiState(
                weight = weight,
                reps = reps
            )
        )
    }

    data class SetUiState(
        val weight : Double = 0.0,
        val reps : Int = 0,
    )

    data class ButtonsUiState(
        val primaryButtonState : PrimaryButtonState = PrimaryButtonState.SAVE,
        val secondaryButtonState : SecondaryButtonState = SecondaryButtonState.CLEAR
    )
}