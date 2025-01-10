package org.connect.fitconnect.domain.workout.usecase

import kotlinx.coroutines.flow.first
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.ExerciseRepository

class GetExerciseForSet(
    private val exerciseRepository: ExerciseRepository,
) {
    suspend operator fun invoke(exerciseId : Int) : Result<Exercise, DataError> {
        return exerciseRepository.getById(exerciseId).first()
    }
}