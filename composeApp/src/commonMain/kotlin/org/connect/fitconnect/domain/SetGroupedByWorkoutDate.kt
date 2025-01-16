package org.connect.fitconnect.domain

import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.Workout

data class SetGroupedByWorkoutDate(
    val id : Int,
    val weight : Double?,
    val reps : Int,
    val workout: Workout,
    val exercise: Exercise
)
