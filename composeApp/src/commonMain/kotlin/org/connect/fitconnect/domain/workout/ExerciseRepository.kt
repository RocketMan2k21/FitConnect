package org.connect.fitconnect.domain.workout

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.ExerciseDto
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.core.Result

interface ExerciseRepository {
    fun fetchAll() : Flow<Result<List<Exercise>, DataError>>
    fun getById(id: Int) : Flow<Result<Exercise, DataError>>
    fun getExerciseByMuscleId(id : Int) : Flow<Result<List<Exercise>, DataError>>
    fun addExercise(exerciseDto: ExerciseDto) : Flow<Result<Exercise, DataError>>
    fun updateExercise(existingExercise: Exercise) : Flow<Result<Exercise, DataError>>
    fun deleteExercise(id: Int) : Flow<Result<Boolean, DataError>>
}