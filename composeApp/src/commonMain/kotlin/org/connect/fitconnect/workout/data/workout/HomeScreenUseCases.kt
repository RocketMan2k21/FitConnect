package org.connect.fitconnect.workout.data.workout

import org.connect.fitconnect.domain.workout.usecase.GetAllWorkoutUseCase
import org.connect.fitconnect.domain.workout.usecase.GetSetsForWorkout
import org.connect.fitconnect.domain.workout.usecase.InitiateNewWorkoutUseCase

data class HomeScreenUseCases (
    val getAllWorkoutsUseCase : GetAllWorkoutUseCase,
    val getSetsForWorkout : GetSetsForWorkout,
    val initiateNewWorkout : InitiateNewWorkoutUseCase
)