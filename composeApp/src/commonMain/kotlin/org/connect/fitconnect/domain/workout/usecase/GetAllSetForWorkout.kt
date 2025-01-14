package org.connect.fitconnect.domain.workout.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.connect.fitconnect.core.Result
import org.connect.fitconnect.core.database.DataError
import org.connect.fitconnect.domain.ExerciseSet
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.domain.workout.SetRepository

class GetSetsForWorkout(
    private val setRepository: SetRepository
) {
    operator fun invoke(id: Int) : Flow<Result<List<ExerciseSet>, DataError>> {
        return setRepository.getAllSets(id)
    }
}