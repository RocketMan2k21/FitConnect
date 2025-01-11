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
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.workout.ExerciseRepository

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository
) : ScreenModel {

    private var _allExercises: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val allExercises : StateFlow<UiState> = _allExercises.asStateFlow()

    var currentExerciseId : MutableState<Long?> = mutableStateOf(null)
        private set

    init {
        fetchAllExercises()
    }

    private fun fetchAllExercises() {
        screenModelScope.launch {
            _allExercises.emit(UiState.Loading)

            val resource = exerciseRepository.fetchAll().first()

            when (resource) {
                is Result.Error -> {
                    _allExercises.emit(UiState.Error("Couldn't Load Exercise Data, please try again later"))
                }
                is Result.Success -> {
                    _allExercises.emit(UiState.Success(resource.data))
                }
            }
        }
    }

    fun onExerciseClick(exerciseId : Long) {
        currentExerciseId.value = exerciseId
    }
}