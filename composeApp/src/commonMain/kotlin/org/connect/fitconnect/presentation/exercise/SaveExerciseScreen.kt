package org.connect.fitconnect.presentation.exercise

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.workout.MuscleGroup
import org.connect.fitconnect.workout.data.exercise.NewExerciseViewModel

object SaveExerciseScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<NewExerciseViewModel>()
        val navigator = LocalNavigator.current

        val uiState by viewModel.uiState.collectAsState()
        val musclesGroups by viewModel.musclesGroupList.collectAsState()
        var selectedMuscleId by remember { mutableStateOf(-1) }

        val isNameError by viewModel.nameError.collectAsState()
        val isMuscleGroupError by viewModel.muscleGroupError.collectAsState()

        val isExerciseAddedSuccessfully by remember{viewModel.isExerciseAdded}

        LaunchedEffect(isExerciseAddedSuccessfully) {
            if (isExerciseAddedSuccessfully) navigator?.pop()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Exercise",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        TextButton(onClick = { navigator?.pop() }) {
                            Text(text = "Cancel")
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            viewModel.onAddNewExerciseClick(selectedMuscleId)
                        }) {
                            Text(text = "Save")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                NewExerciseContent(
                    uiState = uiState,
                    musclesGroups = musclesGroups,
                    isNameError = isNameError,
                    isMuscleGroupError = isMuscleGroupError,
                    onExerciseNameValueChange = viewModel::onExerciseNameChanged,
                    onMuscleGroupValueChange = { muscleGroup: MuscleGroup ->
                        selectedMuscleId = muscleGroup.muscle_number.toInt()
                        viewModel.onMuscleGroupChanged(muscleGroup.name)
                    },
                    onBodyWeightChecked = viewModel::onBodyWeightClick
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private
    @Composable
    fun NewExerciseContent(
        uiState: NewExerciseViewModel.NewExerciseUiState,
        musclesGroups: UiState<List<MuscleGroup>>,
        isNameError: Boolean,
        isMuscleGroupError: Boolean,
        onExerciseNameValueChange: (String) -> Unit,
        onMuscleGroupValueChange: (MuscleGroup) -> Unit,
        onBodyWeightChecked: (Boolean) -> Unit
    ) {

        var isGroupsExpended by remember { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.exerciseName,
            label = { Text("Name") },
            onValueChange = onExerciseNameValueChange,
            isError = isNameError
        )

        Spacer(modifier = Modifier.size(16.dp))

        ExposedDropdownMenuBox(
            expanded = isGroupsExpended,
            onExpandedChange = { isGroupsExpended = !isGroupsExpended }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth(),
                value = uiState.muscleGroupName,
                label = { Text("Muscle Group Category") },
                readOnly = true,
                onValueChange = {},
                isError = isMuscleGroupError,
            )

            ExposedDropdownMenu(
                expanded = isGroupsExpended,
                onDismissRequest = { isGroupsExpended = false }
            ) {
                when (musclesGroups) {
                    is UiState.Error -> {
                        DropdownMenuItem(text = { Text(musclesGroups.message) }, onClick = {})
                    }

                    UiState.Idle -> Unit
                    UiState.Loading -> Unit
                    is UiState.Success -> {
                        musclesGroups.data.forEach { muscleGroup ->
                            DropdownMenuItem(
                                onClick = {
                                    onMuscleGroupValueChange(muscleGroup)
                                    isGroupsExpended = false
                                },
                                text = { Text(muscleGroup.name) },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

            }

        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.isBodyWeight,
                onCheckedChange = onBodyWeightChecked
            )
            Text(
                text = "Body weight",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}