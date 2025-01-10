package org.connect.fitconnect.workout.data.workout

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.workout.Workout
import org.connect.fitconnect.domain.workout.WorkoutRepository
import org.connect.fitconnect.domain.workout.toSet
import org.connect.fitconnect.domain.workout.toWorkout

class WorkoutRepositorySql(
    private val workoutDataSource: WorkoutDataSource
) : WorkoutRepository {

    override fun fetchAll(): Flow<Result<List<Workout>, DataError>> =
        flow {
        workoutDataSource.getAllWorkouts()
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect { dtos ->
                emit(Result.Success(dtos.map { dto -> dto.toWorkout() }))
            }
    }

    override fun deleteWorkout(id: Int): Flow<Result<Boolean, DataError>> =
        flow {
            workoutDataSource.deleteWorkout(id.toLong())
                .catch {
                    emit(Result.Error(DataError.UNKNOWN))
                }.collect {
                    emit(Result.Success(true))
                }

    }

    override fun addWorkout(): Flow<Result<Workout, DataError>> =
        flow {
        workoutDataSource.addWorkout()
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect { dto ->
                emit(Result.Success(dto.toWorkout()))
            }
    }
}