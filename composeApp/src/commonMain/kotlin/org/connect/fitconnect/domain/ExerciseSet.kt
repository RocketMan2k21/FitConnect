package org.connect.fitconnect.domain

data class  ExerciseSet(
    val exerciseId : Long,
    val name : String,
    val weight : Double?,
    val reps : Long
)
