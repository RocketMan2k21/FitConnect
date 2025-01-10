package org.connect.fitconnect.workout.data.exercise

import io.realm.kotlin.internal.interop.RealmSyncSocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.connect.fitconnect.cache.ExerciseDto
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.ExerciseRepository
import org.connect.fitconnect.domain.workout.toExercise
import org.connect.fitconnect.domain.workout.toExerciseDto

class ExerciseRepositorySQL(
    private val dataSource: ExerciseDataSource
) : ExerciseRepository {

    override fun fetchAll() = flow<Result<List<Exercise>, DataError>> {
        dataSource.getAllExercises().catch {
            emit(Result.Error(DataError.UNKNOWN))
        }.collect { dtos ->
            emit(Result.Success(dtos.map { dto -> dto.toExercise() }))
        }
    }

    override fun getById(id : Int): Flow<Result<Exercise, DataError>> =
        flow {
        dataSource.getById(id.toLong()).catch {
            emit(Result.Error(DataError.UNKNOWN))
        }.collect { dto ->
            emit(Result.Success(dto.toExercise()))
        }
    }

    override fun getExerciseByMuscleId(id: Int): Flow<Result<List<Exercise>, DataError>> = flow {
        dataSource.getExerciseByMuscleGroupId(id.toLong()).catch {
            emit(Result.Error(DataError.UNKNOWN))
        }.collect { dtos ->
            emit(Result.Success(dtos.map { dto -> dto.toExercise() }))
        }
    }

    override fun addExercise(exerciseDto: ExerciseDto): Flow<Result<Exercise, DataError>> = flow {
        dataSource.addExercise(exerciseDto)
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect { dto ->
                emit(Result.Success(dto.toExercise()))
            }
    }

    override fun updateExercise(
        existingExercise: Exercise
    ): Flow<Result<Exercise, DataError>> = flow {
        dataSource.updateExercise(existingExercise.toExerciseDto())
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }
            .collect { exerciseDto ->
                emit(Result.Success(exerciseDto.toExercise()))
            }
    }

    override fun deleteExercise(id: Int): Flow<Result<Boolean, DataError>> = flow{
        dataSource.deleteExercise(id.toLong())
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }.collect {
                emit(Result.Success(true))
            }
    }
}