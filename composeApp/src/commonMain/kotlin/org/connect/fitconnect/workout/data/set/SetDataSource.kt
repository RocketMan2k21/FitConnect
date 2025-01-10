package org.connect.fitconnect.workout.data.set

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.SelectSetsForWorkout
import org.connect.fitconnect.cache.SetDto

interface SetDataSource {
    fun getAllSets(workoutId : Long) : Flow<List<SelectSetsForWorkout>>
    fun getById(id: Long) : Flow<SetDto>
    fun insertNewSet(setDto: SetDto) : Flow<SetDto>
    fun updateSet(setDto: SetDto) : Flow<SetDto>
    fun deleteSet(id : Long) : Flow<Unit>
}