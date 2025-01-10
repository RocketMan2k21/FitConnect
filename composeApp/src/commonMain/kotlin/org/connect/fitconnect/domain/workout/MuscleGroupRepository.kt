package org.connect.fitconnect.domain.workout

import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError

interface MuscleGroupRepository {
    fun fetchAll() : Flow<Result<List<MuscleGroup>, DataError>>
}