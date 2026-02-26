package com.wpics.baseballcompass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Provides a dynamic vertical gradient background based on the current weather icon.
 *
 * @param content The UI content to be drawn over the background.
 * @author Justin Fletcher and Yu-Shan "Sami" Sun
 */
@Composable
fun BaseballCompassBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
        content()
    }
}