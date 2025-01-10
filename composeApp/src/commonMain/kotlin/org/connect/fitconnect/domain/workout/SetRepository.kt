package org.connect.fitconnect.domain.workout

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.SelectSetsForWorkout
import org.connect.fitconnect.cache.SetDto
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.domain.ExerciseSet

interface SetRepository {
    fun getAllSets(workoutId : Int) : Flow<Result<List<ExerciseSet>, DataError>>
    fun insertNewSet(setDto: SetDto) : Flow<Result<Set, DataError>>
    fun updateSet(currentSet: SetDto) : Flow<Result<Set, DataError>>
    fun deleteSet(id : Int) : Flow<Result<Boolean, DataError>>
}