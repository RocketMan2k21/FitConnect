package org.connect.fitconnect.workout.data.muscle

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.MuscleGroupDto

interface MuscleDataSource {
    fun getAllMuscleGroups() : Flow<List<MuscleGroupDto>>
}