package org.connect.fitconnect.workout.data.set

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.connect.fitconnect.cache.Database
import org.connect.fitconnect.cache.SelectSetsForWorkout
import org.connect.fitconnect.cache.SetDto

class SetDataSourceSql(database: Database) : SetDataSource {

    private val query = database.getDatabase().fitDatabaseQueries

    override fun getAllSets(workoutId: Long): Flow<List<SelectSetsForWorkout>> {
        return query.selectSetsForWorkout(workoutId).asFlow().mapToList(Dispatchers.IO)
    }

    override fun getById(id: Long): Flow<SetDto> {
        return query.selectSetById(id).asFlow().mapToOne(Dispatchers.IO)
    }

    override fun insertNewSet(
       setDto: SetDto
    ) = run{
        query.insertSet(setDto.workout_id,setDto.exercise_id, setDto.weight, setDto.reps)
        val id = query.selectLastInsertedRowId().executeAsOne()
        getById(id)
    }

    override fun updateSet(setDto: SetDto) = run {
        query.updateSet(setDto.exercise_id, setDto.weight, setDto.reps, setDto.id)
        getById(setDto.id)
    }

    override fun deleteSet(id: Long): Flow<Unit> = flow{
        query.deleteSet(id)
        emit(Unit)
    }
}