package org.connect.fitconnect.workout.data.set

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.ExerciseRepository
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.domain.workout.SetRepository

class SetViewModel(
    private val setRepository: SetRepository,
    private val exerciseRepository: ExerciseRepository
) : ScreenModel {

    private var _currentSetList : MutableStateFlow<UiState<List<Set>>> = MutableStateFlow(UiState.Idle)
    val currentSetList : StateFlow<UiState<List<Set>>> = _currentSetList.asStateFlow()

    var currentSet: MutableState<Set?> = mutableStateOf(null)
        private set

    var currentExerciseId: MutableState<Int> = mutableStateOf(0)
        private set

    var currentWorkoutId: MutableState<Int> = mutableStateOf(0)
        private set

    private var _uiState : MutableStateFlow<SetUiState> = MutableStateFlow(SetUiState())
    val uiState : StateFlow<SetUiState> = _uiState.asStateFlow()

    private var _buttonsUiState : MutableStateFlow<ButtonsUiState> = MutableStateFlow(ButtonsUiState())
    val buttonsUiState : StateFlow<ButtonsUiState> = _buttonsUiState.asStateFlow()

    private val weightIncrease = 0.25
    private val repIncrease = 1

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

    fun updateCurrentSet(set : Set) {
        currentSet.value = set
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

    fun updateExerciseId (exerciseId : Int){
        currentExerciseId.value = exerciseId
    }

    fun updateWorkoutId (workoutId : Int){
        currentWorkoutId.value = workoutId
    }

    private fun onUpdateClick(workoutId: Int, exerciseId: Int) {

        val newSet = currentSet.value?.copy(
            weight = _uiState.value.weight,
            reps = _uiState.value.reps
        )
        newSet?.let {
            screenModelScope.launch {
                val result = setRepository.updateSet(newSet).first()

                when(result) {
                    is Result.Error -> {
                        println("Couldn't not add new set, please try again later")
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

    private fun switchButtons() {
        _buttonsUiState.update {
            ButtonsUiState(
                primaryButtonState = if (_buttonsUiState.value.primaryButtonState == PrimaryButtonState.UPDATE) PrimaryButtonState.SAVE else PrimaryButtonState.UPDATE,
                secondaryButtonState =if (_buttonsUiState.value.secondaryButtonState == SecondaryButtonState.CLEAR) SecondaryButtonState.DELETE else  SecondaryButtonState.CLEAR,
            )
        }
    }

    private  fun updateWeightAndRepsState(weight: Double, reps: Int) {
        _uiState.update {
            SetUiState(
                weight = weight,
                reps = reps
            )
        }
    }

    fun onPrimaryButtonClick() {
        if (currentWorkoutId.value != 0 && currentExerciseId.value != 0) {
            if (buttonsUiState.value.primaryButtonState == PrimaryButtonState.SAVE) {
                onSaveSetClick(currentWorkoutId.value, currentExerciseId.value)
            } else {
                onUpdateClick(currentWorkoutId.value, currentExerciseId.value)
            }
        }
    }

    fun onSecondaryButtonClick() {
        if (currentWorkoutId.value != 0 && currentExerciseId.value != 0) {
            if (buttonsUiState.value.secondaryButtonState == SecondaryButtonState.CLEAR) {
                onClearClick()
            } else {
                onDeleteClick()
            }
        }
    }


    fun onWeightFieldChange(weight: String) {
        try {
            _uiState.update {
                _uiState.value.copy(weight = weight.toDouble())
            }
        } catch (e : Exception) {
            println("Error converting string Weight to double")
        }
    }

    fun onRepsFieldChange(reps: String) {
        try {
            _uiState.update {
                _uiState.value.copy(reps = reps.toInt())
            }
        } catch (e: Exception) {
            println("Error converting string Reps to int")
        }
    }

    fun onAddWeightClick() {
        val currentWeight = _uiState.value.weight
        _uiState.update { _uiState.value.copy(weight = currentWeight + weightIncrease ) }
    }

    fun onMinusWeightClick() {
        val currentWeight = _uiState.value.weight
        _uiState.update { _uiState.value.copy(weight = currentWeight - weightIncrease ) }
    }


    fun onAddRepsClick() {
        val currentReps = _uiState.value.reps
        _uiState.update { _uiState.value.copy(reps = currentReps + repIncrease ) }
    }

    fun onMinusRepsClick() {
        val currentReps = _uiState.value.reps
        _uiState.update { _uiState.value.copy(reps = currentReps - repIncrease ) }
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