package org.connect.fitconnect.workout.data.set

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class PrimaryButtonState(val text : String, val color : Color) {
    SAVE("Save", Color.Green),
    UPDATE("Update", Color.Green)
}

enum class SecondaryButtonState(val text : String, val color : Color) {
    CLEAR("Clear", Color.Blue),
    DELETE("Delete", Color.Red)
}

