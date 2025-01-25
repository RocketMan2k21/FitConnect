package org.connect.fitconnect.presentation.set

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.connect.fitconnect.workout.data.set.PrimaryButtonState
import org.connect.fitconnect.workout.data.set.SecondaryButtonState
import org.connect.fitconnect.workout.data.set.SetViewModel

class ExerciseInsertion(
    private val exerciseName: String,
    private val workoutId: Int,
    private val exerciseId: Int
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<SetViewModel>()

        LaunchedEffect(Unit) {
            viewModel.updateExerciseId(exerciseId)
            viewModel.updateWorkoutId(workoutId)
            viewModel.fetchSetsForExercise(exerciseId, workoutId)
        }

        val sets by viewModel.currentSetList.collectAsState()
        val currentSet by remember {viewModel.currentSet}
        val uiState by viewModel.uiState.collectAsState()
        val buttonsUiState by viewModel.buttonsUiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = exerciseName) },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navigator?.push(
                                SetHistoryScreen(
                                    exerciseId = exerciseId,
                                    exerciseName = exerciseName
                                )
                            )
                        }) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "")
                        }
                    }
                )
            },

        ) { padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Box(
                    modifier = Modifier
                        .padding(padding)
                ) {
                    ExerciseForm(
                        buttonsUiState = buttonsUiState,
                        uiState = uiState,
                        onSecondaryButtonClick = viewModel::onSecondaryButtonClick,
                        onPrimaryButtonClick = viewModel::onPrimaryButtonClick,
                        onAddWeightClick = viewModel::onAddWeightClick,
                        onAddRepsClick = viewModel::onAddRepsClick,
                        onMinusWeightClick = viewModel::onMinusWeightClick,
                        onMinusRepsClick = viewModel::onMinusRepsClick,
                        onWeightValueChange = viewModel::onWeightFieldChange,
                        onRepsValueChange = viewModel::onRepsFieldChange,
                    )
                }
                SetList(
                    setList = sets,
                    onSetClick = { set ->
                        viewModel.updateCurrentSet(set)
                        currentSet?.let {
                            viewModel.onSetClick()
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun ExerciseForm(
        buttonsUiState: SetViewModel.ButtonsUiState,
        uiState: SetViewModel.SetUiState,
        onPrimaryButtonClick : () -> Unit,
        onSecondaryButtonClick: () -> Unit,
        onAddWeightClick : () -> Unit,
        onAddRepsClick : () -> Unit,
        onMinusWeightClick : () -> Unit,
        onMinusRepsClick : () -> Unit,
        onWeightValueChange : (String) -> Unit,
        onRepsValueChange : (String) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeightCounterField(
                    label = "Weight (kg):",
                    uiState = uiState,
                    onAddWeightClick = onAddWeightClick,
                    onMinusWeightClick = onMinusWeightClick,
                    onWeightValueChange = onWeightValueChange,
                )
                Spacer(modifier = Modifier.height(16.dp))
                RepsCounterField(
                    label = "Reps:",
                    uiState = uiState,
                    onAddRepsClick = onAddRepsClick,
                    onMinusRepsClick = onMinusRepsClick,
                    onRepsValueChange = onRepsValueChange,
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onPrimaryButtonClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonsUiState.primaryButtonState.color)
                ) {
                    Text(text = buttonsUiState.primaryButtonState.text)
                }
                Button(
                    onClick = { onSecondaryButtonClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonsUiState.secondaryButtonState.color)
                ) {
                    Text(text = buttonsUiState.secondaryButtonState.text)
                }
            }
        }
    }

    @Composable
    fun WeightCounterField(
        label: String,
        uiState: SetViewModel.SetUiState,
        onAddWeightClick : () -> Unit,
        onMinusWeightClick : () -> Unit,
        onWeightValueChange : (String) -> Unit,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { onMinusWeightClick()}) {
                    Text(text = "-")
                }
                val text = if (uiState.weight % 1 != 0.0) uiState.weight.toString()
                    else uiState.weight.toInt().toString()
                BasicTextField(
                    value = TextFieldValue(
                        text,
                        selection = TextRange(uiState.weight.toString().length)),
                    onValueChange = { onWeightValueChange(it.text) },
                    modifier = Modifier.width(80.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button(onClick = { onAddWeightClick() }) {
                    Text(text = "+")
                }
            }
        }
    }

    @Composable
    fun RepsCounterField(
        label: String,
        uiState: SetViewModel.SetUiState,
        onAddRepsClick : () -> Unit,
        onMinusRepsClick : () -> Unit,
        onRepsValueChange : (String) -> Unit,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { onMinusRepsClick() }) {
                    Text(text = "-")
                }
                BasicTextField(
                    value = TextFieldValue(uiState.reps.toString()),
                    onValueChange = {onRepsValueChange(it.text) },
                    modifier = Modifier.width(80.dp),
                    singleLine = true
                )
                Button(onClick = { onAddRepsClick()}) {
                    Text(text = "+")
                }
            }
        }
    }
}


