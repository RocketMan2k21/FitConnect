package org.connect.fitconnect.workout.data.exercise

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.ExerciseDto

interface ExerciseDataSource {
    fun getAllExercises() : Flow<List<ExerciseDto>>
    fun getById(id: Long) : Flow<ExerciseDto>
    fun addExercise(exerciseDto: ExerciseDto) : Flow<ExerciseDto>
    fun getExerciseByMuscleGroupId(id : Long) : Flow<List<ExerciseDto>>
    fun updateExercise(exerciseDto: ExerciseDto) : Flow<ExerciseDto>
    fun deleteExercise(id : Long) : Flow<Unit>
}