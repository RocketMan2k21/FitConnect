package org.connect.fitconnect.workout.data.muscle

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import org.connect.fitconnect.cache.Database
import org.connect.fitconnect.cache.MuscleGroupDto

class MuscleDataSourceSql(database: Database) : MuscleDataSource {

    private val query = database.getDatabase().fitDatabaseQueries

    override fun getAllMuscleGroups(): Flow<List<MuscleGroupDto>> {
        return query.selectAllMuscleGroups().asFlow().mapToList(Dispatchers.IO)
    }
}