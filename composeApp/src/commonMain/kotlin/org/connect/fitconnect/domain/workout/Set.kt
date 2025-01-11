package org.connect.fitconnect.domain.workout

data class Set(
    val id: Long,
    val workout_id: Int,
    val exercise_id: Int,
    val weight: Double?,
    val reps: Int
)
