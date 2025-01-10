package org.connect.fitconnect.workout.data.workout

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.WorkoutDto

interface WorkoutDataSource {
    fun getAllWorkouts(): Flow<List<WorkoutDto>>
    fun getById(id: Long) : Flow<WorkoutDto>
    fun deleteWorkout(id : Long) : Flow<Unit>
    fun addWorkout() : Flow<WorkoutDto>
}