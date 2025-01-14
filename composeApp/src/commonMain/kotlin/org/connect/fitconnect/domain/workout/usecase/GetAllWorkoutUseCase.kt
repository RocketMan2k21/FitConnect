package org.connect.fitconnect.domain.workout.usecase

import kotlinx.coroutines.flow.first
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.workout.Workout
import org.connect.fitconnect.domain.workout.WorkoutRepository

class GetAllWorkoutUseCase (
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke() : Result<List<Workout>, DataError> {
        return workoutRepository.fetchAll().first()
    }
}