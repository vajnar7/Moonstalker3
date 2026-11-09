package com.vajnar.moonstalker3.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AlarmIndicator(
    alarmLevel: Int, // 0: No alarm (Green), 1: Warning (Yellow), 2: Critical (Red)
    alarmMessage: String,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    val circleColor = when (alarmLevel) {
        2 -> Color(0xFFE53935) // Red - Critical
        1 -> Color(0xFFFFB300) // Yellow - Warning
        else -> Color(0xFF4CAF50) // Green - No alarm
    }

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(circleColor)
            .clickable { showDialog = true }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Alarm Status") },
            text = { Text(text = alarmMessage) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
