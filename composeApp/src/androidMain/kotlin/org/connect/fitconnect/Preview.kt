package org.connect.fitconnect

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.connect.fitconnect.core.CategoryUiState
import org.connect.fitconnect.domain.ExerciseSet
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.presentation.exercise.CategoryItem
import org.connect.fitconnect.presentation.exercise.ExerciseItemName
import org.connect.fitconnect.presentation.home.ExerciseItem
import org.connect.fitconnect.presentation.set.SetItem

@Preview
@Composable
fun PreviewExerciseItem() {
    ExerciseItem(
        exerciseName = "Squats",
        exerciseSetList = listOf(
            ExerciseSet(
                exerciseId = 1,
                name = "Squats",
                reps = 12,
                weight = 25.0
            )
        ),
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewExerciseName() {
    ExerciseItemName(Exercise(
        id = 0L,
        name = "Squats",
        is_body_weight = 1L,
        muscle_group_id = 1
    )) { }
}

@Preview
@Composable
fun SetItemPreview() {
    SetItem(
        set = Set(
            exercise_id = 2,
            id = 2,
            reps = 3,
            weight = 5.5,
            workout_id = 2
        ),
        setIndex = 2,
        isClicked = true
    ) { }
}

@Preview
@Composable
fun PreviewWeekCalendar() {

}

@Preview
@Composable
fun PreviewCategory() {
    CategoryItem(
        categoryUiState = CategoryUiState(
            name = "Back",
            id = 9L,
            color = CategoryUiState.getColorForMuscleGroup("Back"),
            isSelected = false
        ),
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewCategorySelected() {
    CategoryItem(
        categoryUiState = CategoryUiState(
            name = "Chest",
            id = 9L,
            color = CategoryUiState.getColorForMuscleGroup("Chest"),
            isSelected = true
        ),
        onClick = {}
    )
}