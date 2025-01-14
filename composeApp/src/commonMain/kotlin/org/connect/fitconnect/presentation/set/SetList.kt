package org.connect.fitconnect.presentation.set

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.connect.fitconnect.core.UiState
import org.connect.fitconnect.domain.workout.Set
import org.connect.fitconnect.ui.setHomeScreenTextColor

@Composable
fun SetList(
    setList: UiState<List<Set>>,
    onSetClick: (Set) -> Unit
) {

    var isClickedIndex by remember { mutableStateOf(-1) }

    when (setList) {
        is UiState.Error -> Unit
        UiState.Idle -> Unit
        UiState.Loading -> Unit
        is UiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                val data = setList.data
                items(count = data.size) { index ->
                    SetItem(
                        set = data[index], setIndex = index.plus(1), onSetClick = {
                            onSetClick(it)
                            if (isClickedIndex == index)
                                isClickedIndex = -1
                            else
                                isClickedIndex = index
                        },
                        isClicked = isClickedIndex == index
                    )
                }
            }
        }
    }
}

@Composable
fun SetItem(
    isClicked: Boolean,
    set: Set,
    setIndex: Int,
    onSetClick: (Set) -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = if (!isClicked) MaterialTheme.colorScheme.primaryContainer else setHomeScreenTextColor.copy(alpha = 0.5f))
            .clickable { onSetClick(set) }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = setIndex.toString(),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = set.weight.toString(), style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.size(4.dp))
            Text(
                text = "kg",
                style = MaterialTheme.typography.labelMedium,
                color = setHomeScreenTextColor
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = set.reps.toString(), style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.size(4.dp))
            Text(
                text = "reps",
                style = MaterialTheme.typography.labelMedium,
                color = setHomeScreenTextColor
            )
        }

    }
}
