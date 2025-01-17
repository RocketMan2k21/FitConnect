package org.connect.fitconnect.presentation.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.connect.fitconnect.core.CategoryUiState
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
        val categoryList by viewModel.categoryList.collectAsState()
        var selectedCategoryId by remember { mutableStateOf(-1L) }

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
                    },
                    actions = {
                        IconButton(onClick = { navigator?.push(SaveExerciseScreen) }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
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
                ExerciseList(
                    exerciseList = exerciseList,
                    onClick = { exercise ->
                        navigator?.push(
                            ExerciseInsertion(
                                exerciseName = exercise.name,
                                exerciseId = exercise.id.toInt(),
                                workoutId = currentWorkoutId
                            )
                        )
                    },
                    categoryList = categoryList,
                    onCategoryFilterSelected = {
                        viewModel.onCategoryFilterSelect(it)
                        if (selectedCategoryId != it)
                            selectedCategoryId = it
                        else
                            selectedCategoryId = -1
                    },
                    selectedCategoryId = selectedCategoryId
                )
            }
        }
    }


}

@OptIn(ExperimentalLayoutApi::class)
private
@Composable
fun CategoryRow(
    modifier: Modifier = Modifier,
    categoryList: List<CategoryUiState>,
    onSelected: (Long) -> Unit
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categoryList.forEach {
            CategoryItem(
                onClick = onSelected,
                categoryUiState = it
            )
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    categoryUiState: CategoryUiState,
    onClick: (Long) -> Unit
) {
    AssistChip(
        modifier = modifier,
        label = { Text(categoryUiState.name) },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .background(shape = CircleShape, color = categoryUiState.color)
            )
        },
        onClick = {
            onClick(categoryUiState.id)
        },
        colors = AssistChipDefaults.assistChipColors().copy(
            containerColor = if (categoryUiState.isSelected) Color.Gray
            else AssistChipDefaults.assistChipColors().containerColor,
            labelColor = if (categoryUiState.isSelected) Color.White else AssistChipDefaults.assistChipColors().labelColor
        )
    )
}

@Composable
fun ExerciseList(
    modifier: Modifier = Modifier,
    exerciseList: ExerciseViewModel.ExerciseListUiState,
    onClick: (Exercise) -> Unit,
    categoryList: List<CategoryUiState>,
    onCategoryFilterSelected: (Long) -> Unit,
    selectedCategoryId : Long
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CategoryRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                categoryList = categoryList,
                onSelected = onCategoryFilterSelected
            )
        }

        when (exerciseList) {
            is ExerciseViewModel.ExerciseListUiState.Error -> {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = exerciseList.message)
                    }
                }
            }

            ExerciseViewModel.ExerciseListUiState.Idle -> Unit
            ExerciseViewModel.ExerciseListUiState.Loading -> Unit
            is ExerciseViewModel.ExerciseListUiState.Success -> {
                val data =
                if (selectedCategoryId != -1L )
                    exerciseList.data.filter { selectedCategoryId == it.muscle_group_id }
                else {
                    exerciseList.data
                }

                if (data.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No exercise for category")
                        }
                    }
                }

                items(data) {
                    ExerciseItemName(
                        exercise = it,
                        onClick
                    )
                }
            }
        }
    }
}


@Composable
fun ExerciseItemName(
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
            modifier = Modifier
                .padding(start = 16.dp),
            text = exercise.name,
            style = MaterialTheme.typography.displaySmall
        )
    }
}
