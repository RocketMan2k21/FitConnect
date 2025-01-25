
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Duration = 500.milliseconds,
    content: @Composable (T) -> Unit
) {
    var shouldDelete by remember {
        mutableStateOf(false)
    }
    var shouldCancel by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeToDismissBoxState(
        positionalThreshold = {
            600f // Derived thru trial and error
        }

    )

    // Triggers the command to delete the item
    LaunchedEffect(key1 = shouldDelete) {
        if(shouldDelete) {
            delay(animationDuration)
            onDelete(item)
            shouldDelete = false
        }
    }

    // Triggers the command to cancel the delete
    LaunchedEffect(key1 = shouldCancel) {
        if(shouldCancel) {
            state.reset()
            shouldCancel = false
        }
    }

    AnimatedVisibility(
        visible = !shouldDelete,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration.toInt(DurationUnit.MILLISECONDS)),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(
                    swipeDismissState = state,
                    onDelete = {
                        shouldDelete = true
                    },
                    onCancel = {
                        shouldCancel = true
                    }
                )
            },
            content = { content(item) },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true
        )
    }
}

@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState,
    onDelete: () -> Unit = { },
    onCancel: () -> Unit = { }
) {
    val color = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else
        Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row {
            Text(
                "Delete this exercise?",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onError
            )
            Spacer(modifier = Modifier.width(20.dp))

            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier.clickable {
                    onCancel()
                }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier.clickable {
                    onDelete()
                }
            )
        }
    }
}