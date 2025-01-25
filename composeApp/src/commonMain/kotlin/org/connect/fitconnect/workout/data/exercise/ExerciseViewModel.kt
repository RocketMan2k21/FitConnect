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
import org.connect.fitconnect.core.CategoryUiState
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.ExerciseRepository
import org.connect.fitconnect.domain.workout.MuscleGroupRepository

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val muscleGroupRepository: MuscleGroupRepository
) : ScreenModel {

    private var _allExercises: MutableStateFlow<ExerciseListUiState> =
        MutableStateFlow(ExerciseListUiState.Idle)
    val allExercises: StateFlow<ExerciseListUiState> = _allExercises.asStateFlow()

    var currentExerciseId: MutableState<Long?> = mutableStateOf(null)
        private set

    var currentExercise: MutableState<Exercise?> = mutableStateOf(null)
        private set

    private val _categoryList: MutableStateFlow<List<CategoryUiState>> =
        MutableStateFlow(CategoryUiState.Init)
    val categoryList: StateFlow<List<CategoryUiState>> = _categoryList.asStateFlow()

    init {
        fetchAllExercises()
        fetchMuscleGroups()
    }

    private fun fetchMuscleGroups() {
        screenModelScope.launch {
            when (val result = muscleGroupRepository.fetchAll().first()) {
                is Result.Success -> {
                    _categoryList.emit(CategoryUiState.getCategoriesForUi(result.data))
                }

                else -> Unit
            }
        }
    }

    private fun fetchAllExercises() {
        screenModelScope.launch {
            _allExercises.emit(ExerciseListUiState.Loading)

            exerciseRepository.fetchAll().collectLatest { resource ->
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
    }

    private fun updateExerciseList(categoryId: Long) {
        if (_allExercises.value is ExerciseListUiState.Success && categoryId != -1L) {
            val data = (_allExercises.value as ExerciseListUiState.Success).data
            val list = data
                .filter { it.muscle_group_id == categoryId }

            if (list.isNotEmpty()) {
                _allExercises.update { ExerciseListUiState.Success(list) }
            } else {
                _allExercises.update { ExerciseListUiState.Error("No exercise for category") }
            }
        }
    }


    fun onCategoryFilterSelect(newCategoryId : Long) {
        _categoryList.update { state ->
            state.map { category ->
                if (category.id == newCategoryId) {
                    if (!category.isSelected) {
                        category.copy(isSelected = true)
                    } else {
                        category.copy(isSelected = false)
                    }
                } else {
                    category.copy(isSelected = false)
                }
            }
        }
    }

    fun onDragDelete(exerciseId : Int) {
        screenModelScope.launch {
            exerciseRepository.deleteExercise(id = exerciseId)
                .first()

            when(val result = exerciseRepository.deleteExercise(id = exerciseId)
                .first()) {
                is Result.Error -> Unit
                is Result.Success -> {
                    println("Exercise $exerciseId was deleted successfully")
                }
            }
        }
    }

    fun onSearchQueryType(newString : String) {
        if (_allExercises.value is ExerciseListUiState.Success && newString.isNotBlank()) {
            val filteredExerciseList = (_allExercises.value as ExerciseListUiState.Success).data
                .filter { it.name == newString }

            if (filteredExerciseList.isNotEmpty()) {
                _allExercises.update {
                    ExerciseListUiState.Success(filteredExerciseList)
                }
            }
            else {
                _allExercises.update {
                    ExerciseListUiState.Error("No such exercises")
                }
            }
        }
    }
    sealed class ExerciseListUiState{
        data object Idle : ExerciseListUiState()
        data object Loading : ExerciseListUiState()
        data class Success(val data : List<Exercise>) : ExerciseListUiState()
        data class Error(val message : String) :  ExerciseListUiState()
    }
}