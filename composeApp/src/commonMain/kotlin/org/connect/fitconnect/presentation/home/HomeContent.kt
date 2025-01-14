package org.connect.fitconnect.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.connect.fitconnect.domain.ExerciseSet
import org.connect.fitconnect.ui.setHomeScreenTextColor

@Composable
fun HomeBody(
    workoutStateList: List<ExerciseSet>, onExerciseClick: (ExerciseSet) -> Unit
) {

    val exerciseNames by derivedStateOf { workoutStateList.distinctBy { it.name } }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        exerciseNames.forEach { distinctedSet ->
            val filteredList = workoutStateList.filter { it.name == distinctedSet.name }

            item {
                ExerciseItem(distinctedSet.name, filteredList, onClick = onExerciseClick)
            }
        }
    }
}


@Composable
fun ExerciseItem(
    exerciseName: String, exerciseSetList: List<ExerciseSet>, onClick: (ExerciseSet) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick(exerciseSetList.first()) },
        shape = RoundedCornerShape(0.dp)
    ) {
        Column(
            Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = exerciseName, style = MaterialTheme.typography.headlineLarge
                )
            }
            exerciseSetList.forEachIndexed { index, set ->
                Row(
                    modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.End),
                    horizontalArrangement = Arrangement.SpaceBetween,

                ) {

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = set.weight.toString(),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "kg",
                            color = setHomeScreenTextColor,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Row (
                        verticalAlignment = Alignment.Bottom
                    ){
                        Text(
                            text = set.reps.toString(),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "reps",
                            color = setHomeScreenTextColor,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

        }
    }
}


