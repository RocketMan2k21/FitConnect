package org.connect.fitconnect.workout.data.exercise

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connect.fitconnect.cache.Database
import org.connect.fitconnect.cache.ExerciseDto

class ExerciseDataSourceSql(database: Database) : ExerciseDataSource {

    private val query = database.getDatabase().fitDatabaseQueries

    override fun getAllExercises(): Flow<List<ExerciseDto>> {
        return query.selectAllExercises().asFlow().mapToList(Dispatchers.IO)
    }

    override fun getById(id: Long): Flow<ExerciseDto> {
        return query.selectExerciseById(id).asFlow().mapToOne(Dispatchers.IO)
    }

    override fun addExercise(exerciseDto: ExerciseDto) = run {
        query.insertExercise(
            name = exerciseDto.name,
            is_body_weight = exerciseDto.is_body_weight,
            muscle_group_id = exerciseDto.muscle_group_id
        )
        val id = query.selectLastInsertedRowId().executeAsOne()
        getById(id)
    }

    override fun getExerciseByMuscleGroupId(id: Long): Flow<List<ExerciseDto>> {
        return query.selectExercisesByMuscleGroup(id).asFlow().mapToList(Dispatchers.IO)
    }

    override fun updateExercise(
        exerciseDto: ExerciseDto
    ) = run {
        query.updateExercise(
            name = exerciseDto.name,
            id = exerciseDto.id,
            is_body_weight = exerciseDto.is_body_weight,
            muscle_group_id = exerciseDto.muscle_group_id
        )
       getById(exerciseDto.id)
    }

    override fun deleteExercise(id: Long): Flow<Unit> = flow {
        query.deleteExercise(id)
        emit(Unit)
    }
}