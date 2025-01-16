package org.connect.fitconnect.domain.workout

import org.connect.fitconnect.cache.ExerciseDto
import org.connect.fitconnect.cache.MuscleGroupDto
import org.connect.fitconnect.cache.SelectSetsForWorkout
import org.connect.fitconnect.cache.SelectWorkoutsWithExercisesAndSetsGroupedByDate
import org.connect.fitconnect.cache.SetDto
import org.connect.fitconnect.cache.WorkoutDto
import org.connect.fitconnect.domain.ExerciseSet
import org.connect.fitconnect.domain.SetGroupedByWorkoutDate

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
        id, workout_id.toInt(), exercise_id.toInt(), weight, reps.toInt()
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
        workout_id = workout_id.toLong(),
        exercise_id = exercise_id.toLong(),
        weight = weight,
        reps = reps.toLong()
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
        exerciseId = id,
        name = exercise_name,
        weight = weight,
        reps = reps
    )
}

fun SelectWorkoutsWithExercisesAndSetsGroupedByDate.toSetGroupedByWorkoutDate () : SetGroupedByWorkoutDate {
    return SetGroupedByWorkoutDate(
        id = set_id.toInt(),
        weight = weight,
        reps = reps.toInt(),
        exercise = Exercise(
            id = exercise_id,
            name = exercise_name,
            is_body_weight = is_body_weight,
            muscle_group_id = muscle_group
        ),
        workout = Workout(
            id = workout_id,
            date = workout_date
        )
    )
}



