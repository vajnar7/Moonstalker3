package com.vajnar.moonstalker3.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vajnar.moonstalker3.processor.OpCodes

fun getStatusName(status: Int): String {
    return when (status) {
        OpCodes.NOT_CONNECTED -> "Not Connected"
//        OpCodes.RESET -> "Resetting"
        OpCodes.ERROR -> "Error"
        OpCodes.CONNECTING -> "Connecting"
//        OpCodes.PING -> "Ping"
        OpCodes.READY -> "Ready"
        OpCodes.CONNECTED -> "Connected"
        else -> "Status ($status)"
    }
}

fun getStatusColor(status: Int): Color {
    return when (status) {
        OpCodes.NOT_CONNECTED -> Color(0xFF78909C) // Blue Grey
//        OpCodes.RESET -> Color(0xFFFF9800)        // Orange akcija ni stanje
        OpCodes.ERROR -> Color(0xFFE53935)        // Red
        OpCodes.CONNECTING -> Color(0xFFFBC02D)   // Yellow
//        OpCodes.PING -> Color(0xFF00ACC1)         // Cyan akcija ni stanje
        OpCodes.READY -> Color(0xFF43A047)        // Green
        OpCodes.CONNECTED -> Color(0xFF1E88E5)    // Blue
        else -> Color(0xFF9E9E9E)                 // Grey
    }
}

@Composable
fun Status(
    status: Int,
    infoMessage: String,
    modifier: Modifier = Modifier,
) {
    val statusName = getStatusName(status)
    val statusColor = getStatusColor(status)

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "System Status",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = statusName,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "Info",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = infoMessage.ifEmpty { "Idle" },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
