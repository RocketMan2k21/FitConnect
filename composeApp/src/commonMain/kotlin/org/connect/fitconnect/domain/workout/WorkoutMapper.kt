package org.connect.fitconnect.domain.workout

import org.connect.fitconnect.cache.ExerciseDto
import org.connect.fitconnect.cache.MuscleGroupDto
import org.connect.fitconnect.cache.SelectSetsForWorkout
import org.connect.fitconnect.cache.SetDto
import org.connect.fitconnect.cache.WorkoutDto
import org.connect.fitconnect.domain.ExerciseSet

fun ExerciseDto.toExercise() : Exercise {
    return Exercise (
        id = id,
        is_body_weight = is_body_weight,
        muscle_group_id = muscle_group_id,
        name = name
    )
}

fun MuscleGroupDto.toMuscleGroup() : MuscleGroup {
    return MuscleGroup(
        muscle_number, name
    )
}

fun SetDto.toSet() : Set {
    return Set(
        id, workout_id, exercise_id, weight, reps
    )
}

fun WorkoutDto.toWorkout() : Workout {
    return Workout(id, date)
}

fun Exercise.toExerciseDto(): ExerciseDto {
    return ExerciseDto(
        id = id,
        is_body_weight = is_body_weight,
        muscle_group_id = muscle_group_id,
        name = name
    )
}

fun MuscleGroup.toMuscleGroupDto(): MuscleGroupDto {
    return MuscleGroupDto(
        muscle_number = muscle_number,
        name = name
    )
}

fun Set.toSetDto(): SetDto {
    return SetDto(
        id = id,
        workout_id = workout_id,
        exercise_id = exercise_id,
        weight = weight,
        reps = reps
    )
}

fun Workout.toWorkoutDto(): WorkoutDto {
    return WorkoutDto(
        id = id,
        date = date
    )
}

fun SelectSetsForWorkout.toExerciseSet() : ExerciseSet {
    return ExerciseSet(
        name = exercise_name,
        weight = weight,
        reps = reps
    )
}



