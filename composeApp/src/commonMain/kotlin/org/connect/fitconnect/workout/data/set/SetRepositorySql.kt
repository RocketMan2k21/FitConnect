package org.connect.fitconnect.workout.data.set

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.connect.fitconnect.cache.SelectSetsForWorkout
import org.connect.fitconnect.cache.SetDto
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.ExerciseSet
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.domain.workout.SetRepository
import org.connect.fitconnect.domain.workout.toExerciseSet
import org.connect.fitconnect.domain.workout.toSet
import org.connect.fitconnect.domain.workout.toSetDto

class SetRepositorySql(
    private val setDataSource: SetDataSource
) : SetRepository {

    override fun getAllSets(workoutId: Int): Flow<Result<List<ExerciseSet>, DataError>> = flow {
        setDataSource.getAllSets(workoutId.toLong())
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect { dtos ->
                emit(Result.Success(dtos.map { dto -> dto.toExerciseSet() }))
            }
    }

    override fun getAllSetsForExerciseAndWorkout(workoutId: Int, exerciseId: Int): Flow<Result<List<Set>, DataError>> = flow {
        setDataSource.getSetsForWorkoutAndExercise(workoutId.toLong(), exerciseId.toLong())
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect { dtos ->
                emit(Result.Success(dtos.map { dto -> dto.toSet() }))
            }
    }

    override fun insertNewSet(
        set: Set
    ): Flow<Result<Set, DataError>> = flow {
        setDataSource.insertNewSet(set.toSetDto())
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect { dto ->
                emit(Result.Success(dto.toSet()))
            }
    }

    override fun updateSet(currentSet: Set): Flow<Result<Set, DataError>> = flow {
        setDataSource.updateSet(currentSet.toSetDto())
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect { dto ->
                emit(Result.Success(dto.toSet()))
            }
    }

    override fun deleteSet(id: Int): Flow<Result<Boolean, DataError>> =
        flow {
            setDataSource.deleteSet(id.toLong())
                .catch {
                    emit(Result.Error(DataError.UNKNOWN))
                }.collect { dto ->
                    emit(Result.Success(true))
                }
        }
}