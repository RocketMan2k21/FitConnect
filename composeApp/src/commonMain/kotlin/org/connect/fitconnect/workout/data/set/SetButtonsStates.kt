package org.connect.fitconnect.workout.data.set

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class PrimaryButtonState(name : String, color : Int) {
    SAVE("Save", Color.Green.toArgb()),
    UPDATE("Update", Color.Green.toArgb())
}

enum class SecondaryButtonState(name : String, color : Int) {
    CLEAR("Clear", Color.Blue.toArgb()),
    DELETE("Delete", Color.Red.toArgb())
}

