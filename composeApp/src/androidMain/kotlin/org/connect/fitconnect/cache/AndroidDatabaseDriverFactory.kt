package org.connect.fitconnect.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import core.initializer.AppContextWrapper
import org.connect.fitconnect.FitDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(FitDatabase.Schema, AppContextWrapper.appContext!!, "launch.db")
    }
}