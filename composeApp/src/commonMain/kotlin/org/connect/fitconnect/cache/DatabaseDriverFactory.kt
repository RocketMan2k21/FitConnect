package org.connect.fitconnect.cache

import app.cash.sqldelight.db.SqlDriver
import org.connect.fitconnect.FitDatabase

expect class DriverFactory() {
    fun createDriver(): SqlDriver
    }

fun createDatabase(driverFactory: DriverFactory): FitDatabase {
    return FitDatabase(driverFactory.createDriver())
}