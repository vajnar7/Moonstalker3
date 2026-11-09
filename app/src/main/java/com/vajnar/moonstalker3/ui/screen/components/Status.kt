package com.vajnar.moonstalker3.ui.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Status(
    status: Int,
    infoMessage: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Status: $status",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Info: $infoMessage",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}