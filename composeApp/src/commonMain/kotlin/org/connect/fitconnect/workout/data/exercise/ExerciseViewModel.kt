package org.connect.fitconnect.workout.data.exercise

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.ExerciseRepository

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository
) : ScreenModel {

    private var _allExercises: MutableStateFlow<ExerciseListUiState> = MutableStateFlow(ExerciseListUiState.Idle)
    val allExercises : StateFlow<ExerciseListUiState> = _allExercises.asStateFlow()

    var currentExerciseId : MutableState<Long?> = mutableStateOf(null)
        private set

    var currentExercise : MutableState<Exercise?> = mutableStateOf(null)
        private set

    init {
        fetchAllExercises()
    }

    private fun fetchAllExercises() {
        screenModelScope.launch {
            _allExercises.emit(ExerciseListUiState.Loading)

            val resource = exerciseRepository.fetchAll().first()

            when (resource) {
                is Result.Error -> {
                    _allExercises.emit(ExerciseListUiState.Error("Couldn't Load Exercise Data, please try again later"))
                }
                is Result.Success -> {
                    _allExercises.emit(ExerciseListUiState.Success(resource.data))
                }
            }
        }
    }

    fun onExerciseClick(exerciseId : Long) {
        currentExerciseId.value = exerciseId
    }

    sealed class ExerciseListUiState{
        data object Idle : ExerciseListUiState()
        data object Loading : ExerciseListUiState()
        data class Success(val data : List<Exercise>) : ExerciseListUiState()
        data class Error(val message : String) :  ExerciseListUiState()
    }

}