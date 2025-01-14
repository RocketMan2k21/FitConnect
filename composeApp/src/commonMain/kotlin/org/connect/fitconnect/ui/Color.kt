package org.connect.fitconnect.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val setHomeScreenTextColor : Color
    @Composable
    get() = if(!isSystemInDarkTheme()) Color.Gray
    else Color.Gray