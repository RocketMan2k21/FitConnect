package org.connect.fitconnect.presentation.set

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.datetime.LocalDate
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.SetGroupedByWorkoutDate
import org.connect.fitconnect.domain.workout.Exercise
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.utils.DateUtils
import org.connect.fitconnect.workout.data.history.SetHistoryViewModel

class SetHistoryScreen(
    private val exerciseId: Int,
    private val exerciseName: String
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<SetHistoryViewModel>()
        val sets by viewModel.setHistoryList.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getAllSetByExerciseId(exerciseId = exerciseId)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = exerciseName)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                SetContent(
                    list = sets
                )
            }
        }
    }
}

private @Composable
fun SetContent(modifier: Modifier = Modifier, list: UiState<List<SetGroupedByWorkoutDate>>) {
    when (list) {
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = list.message)
            }
        }
        UiState.Idle -> Unit
        UiState.Loading -> Unit
        is UiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val groupedByDate = list.data.groupBy { it.workout.date }
                groupedByDate.forEach { (date, setList) ->
                    item {
                        Row(
                            modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        ) {
                            val inputDate = DateUtils.parseDateFromDatabase(date)
                            Text(text = "${inputDate.dayOfWeek.name}, ${inputDate.month.name} ${inputDate.dayOfMonth}, ${inputDate.year}")
                        }
                        setList.forEachIndexed { index, set ->
                            SetItem(
                                isClicked = false,
                                onSetClick = {},
                                setIndex = index,
                                set = Set(
                                    id = set.id.toLong(),
                                    exercise_id = set.exercise.id.toInt(),
                                    reps = set.reps,
                                    workout_id = set.workout.id.toInt(),
                                    weight = set.weight
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


