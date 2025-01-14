package org.connect.fitconnect.presentation.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.presentation.set.ExerciseInsertion
import org.connect.fitconnect.workout.data.exercise.ExerciseViewModel

class ExerciseScreen(private val currentWorkoutId: Int) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<ExerciseViewModel>()

        val exerciseList by viewModel.allExercises.collectAsState()
        val currentExerciseId by remember { viewModel.currentExerciseId }
        val currentExercise by remember { viewModel.currentExercise }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Choose Exercise") },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Arrow back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (exerciseList) {
                    is ExerciseViewModel.ExerciseListUiState.Error -> Unit
                    ExerciseViewModel.ExerciseListUiState.Idle -> Unit
                    ExerciseViewModel.ExerciseListUiState.Loading -> Unit
                    is ExerciseViewModel.ExerciseListUiState.Success -> {
                        ExerciseList(
                            list = (exerciseList as ExerciseViewModel.ExerciseListUiState.Success).data,
                            onClick = { exercise ->
                                    navigator?.push(
                                        ExerciseInsertion(
                                            exerciseName = exercise.name,
                                            exerciseId = exercise.id.toInt(),
                                            workoutId = currentWorkoutId
                                        )
                                    )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseList(
    modifier: Modifier = Modifier,
    list: List<Exercise>,
    onClick: (Exercise) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) {
            ExerciseItem(
                exercise = it,
                onClick
            )
        }
    }
}


@Composable
fun ExerciseItem(
    exercise: Exercise,
    onClick: (Exercise) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onClick(exercise)
            }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.displayMedium
        )
    }
}
