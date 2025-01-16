package org.connect.fitconnect.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.data.local.WeekCalendarDataSource
import org.connect.fitconnect.di.initializeKoin
import org.connect.fitconnect.domain.ExerciseSet
import org.connect.fitconnect.presentation.exercise.ExerciseScreen
import org.connect.fitconnect.presentation.set.ExerciseInsertion
import org.connect.fitconnect.workout.data.workout.HomeScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinScreenModel<HomeScreenViewModel>()
        val currentWorkout by viewModel.currentWorkout.collectAsState()
        val workoutId by viewModel.currentWorkoutId.collectAsState()

        val isCurrentWorkoutPresent by derivedStateOf { workoutId != -1L }

        var navigateToExerciseScreen by remember { mutableStateOf(false) }

        LaunchedEffect(workoutId, navigateToExerciseScreen) {
            if (navigateToExerciseScreen && workoutId != -1L) {

                navigator?.push(ExerciseScreen(currentWorkoutId = workoutId.toInt()))
                navigateToExerciseScreen = false
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "FitConnect") },
                )
            },
            floatingActionButton = {
                MainWorkoutButton(
                    onClick = {
                        if (!isCurrentWorkoutPresent) {
                            viewModel.onAddWorkoutClick()
                            navigateToExerciseScreen = true
                        } else {
                            navigator?.push(ExerciseScreen(currentWorkoutId = workoutId.toInt()))
                        }
                    },
                    isCurrentWorkoutPresent = isCurrentWorkoutPresent,
                )
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                CalendarView(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp),
                    onSelectedDate = { date ->
                        viewModel.onDateSelectedClick(date)
                    }
                )
                when (currentWorkout) {
                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = (currentWorkout as UiState.Error).message)
                        }
                    }
                    UiState.Idle -> Unit
                    UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        HomeBody(
                            workoutStateList = (currentWorkout as UiState.Success<List<ExerciseSet>>).data,
                            onExerciseClick = { set ->
                                viewModel.currentWorkoutId.value?.let {
                                    navigator?.push(
                                        ExerciseInsertion(
                                            exerciseName = set.name,
                                            exerciseId = set.exerciseId.toInt(),
                                            workoutId = it.toInt()
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainWorkoutButton(
    isCurrentWorkoutPresent: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
    ) {
        Text(
            text = if (!isCurrentWorkoutPresent) "Start Workout" else "Add Exercise",
            style = MaterialTheme.typography.titleMedium
        )
    }
}