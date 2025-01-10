package org.connect.fitconnect.workout.data.muscle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.workout.MuscleGroup
import org.connect.fitconnect.domain.workout.MuscleGroupRepository
import org.connect.fitconnect.domain.workout.toMuscleGroup

class MuscleRepositorySql(
    private val muscleDataSource: MuscleDataSource
) : MuscleGroupRepository {
    override fun fetchAll(): Flow<Result<List<MuscleGroup>, DataError>> =
        flow {
        muscleDataSource.getAllMuscleGroups()
            .catch {
                emit(Result.Error(DataError.UNKNOWN))
            }
            .collect { dtos ->
                dtos.map { dto -> dto.toMuscleGroup() }
            }
    }
}