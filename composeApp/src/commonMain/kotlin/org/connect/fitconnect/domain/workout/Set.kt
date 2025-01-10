package org.connect.fitconnect.domain.workout

data class Set(
    val id: Long,
    val workout_id: Long,
    val exercise_id: Long,
    val weight: Double?,
    val reps: Long
)
