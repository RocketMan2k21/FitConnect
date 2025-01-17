package org.connect.fitconnect.workout.data.exercise

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.ExerciseRepository
import org.connect.fitconnect.domain.workout.MuscleGroup
import org.connect.fitconnect.domain.workout.MuscleGroupRepository

class NewExerciseViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val muscleGroupRepository: MuscleGroupRepository
) : ScreenModel {
    
    private val _musclesGroupList : MutableStateFlow<UiState<List<MuscleGroup>>> = MutableStateFlow(UiState.Idle)
    val musclesGroupList : StateFlow<UiState<List<MuscleGroup>>> = _musclesGroupList.asStateFlow()

    private val _uiState: MutableStateFlow<NewExerciseUiState> = MutableStateFlow(NewExerciseUiState.Init)
    val uiState : StateFlow<NewExerciseUiState> = _uiState.asStateFlow()

    private val _nameError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val nameError : StateFlow<Boolean> = _nameError.asStateFlow()

    private val _muscleGroupError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val muscleGroupError : StateFlow<Boolean> = _muscleGroupError.asStateFlow()

    var isExerciseAdded : MutableState<Boolean> = mutableStateOf(false)
        private set

    init {
        fetchMuscleGroups()
    }
    
    private fun fetchMuscleGroups() {
        screenModelScope.launch {
            when(val result = muscleGroupRepository.fetchAll().first()) {
                is Result.Error -> {
                    _musclesGroupList.emit(UiState.Error("Couldn't fetch category list, please try again"))
                }
                is Result.Success -> {
                    _musclesGroupList.emit(UiState.Success(result.data))
                }
            }
        }
    }
    
    fun onAddNewExerciseClick(muscleGroupId : Int) {
        if(_uiState.value.exerciseName.isBlank()) {
            _nameError.update { true }
        }

        if(_uiState.value.muscleGroupName.isBlank() || muscleGroupId == -1) {
            _muscleGroupError.update { true }
        }

        if (_nameError.value || muscleGroupError.value) {
            return
        }

        saveNewExercise(muscleGroupId)
    }

    private fun saveNewExercise(muscleGroupId: Int) {
        screenModelScope.launch {
            exerciseRepository.addExercise(
                Exercise(
                    id = 0,
                    is_body_weight = if (_uiState.value.isBodyWeight) 1 else 0,
                    muscle_group_id = muscleGroupId.toLong(),
                    name = _uiState.value.exerciseName
                )
            ).collectLatest {
                when (it) {
                    is Result.Error -> {
                        println("Exercise ${it.error}")
                        isExerciseAdded.value = true
                    }
                    is Result.Success -> {
                        isExerciseAdded.value = true
                        println("Exercise ${it.data.id} successfully")
                    }
                }
            }
        }
    }



    fun onExerciseNameChanged(newName : String) {
        _nameError.update { false }
        _uiState.update { state ->
            state.copy(exerciseName = newName)
        }
    }

    fun onBodyWeightClick(isBodyWeight: Boolean) {
        _uiState.update { state ->
            state.copy(isBodyWeight = isBodyWeight)
        }
    }
    
    fun onMuscleGroupChanged(muscleGroup : String) {
        _muscleGroupError.update { false }
        _uiState.update { state ->
            state.copy(muscleGroupName = muscleGroup)
        }
    }

    data class NewExerciseUiState(
        val exerciseName : String,
        val isBodyWeight : Boolean,
        val muscleGroupName : String
    ) {
        companion object{
            val Init = NewExerciseUiState(
                exerciseName = "",
                isBodyWeight = false,
                muscleGroupName = ""
            )
        }
    }
}