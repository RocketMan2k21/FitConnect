package org.connect.fitconnect.workout.data.workout

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connect.fitconnect.FitDatabase
import org.connect.fitconnect.cache.WorkoutDto

class WorkoutDataSourceSql(database: FitDatabase) : WorkoutDataSource {

    private val query = database.fitDatabaseQueries

    override fun getAllWorkouts() =
         query.selectAllWorkouts().asFlow().mapToList(Dispatchers.IO)

    override fun getById(id: Long): Flow<WorkoutDto> {
        return query.selectWorkoutById(id).asFlow().mapToOne(Dispatchers.IO)
    }

    override fun deleteWorkout(id: Long): Flow<Unit> = flow{
        query.deleteWorkout(id)
        emit(Unit)
    }

    override fun addWorkout() = run {
        query.insertWorkout()
        val id = query.selectLastInsertedRowId().executeAsOne()
        getById(id)
    }
}