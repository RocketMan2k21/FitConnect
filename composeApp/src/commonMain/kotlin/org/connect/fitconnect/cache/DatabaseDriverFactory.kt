package org.connect.fitconnect.cache

import app.cash.sqldelight.db.SqlDriver
import org.connect.fitconnect.FitDatabase

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

class Database(private val databaseDriverFactory: DatabaseDriverFactory) {
    fun getDatabase(): FitDatabase {
        return FitDatabase(databaseDriverFactory.createDriver())
    }
}