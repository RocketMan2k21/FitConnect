package org.connect.fitconnect.domain.workout

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.WorkoutDto
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError

interface WorkoutRepository {
    fun fetchAll() : Flow<Result<List<Workout>, DataError>>
    fun deleteWorkout(id : Int) : Flow<Result<Boolean, DataError>>
    fun addWorkout() : Flow<Result<Workout, DataError>>
}