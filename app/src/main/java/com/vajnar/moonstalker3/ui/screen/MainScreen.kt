package com.vajnar.moonstalker3.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vajnar.moonstalker3.processor.OpCodes
import com.vajnar.moonstalker3.ui.screen.components.AlarmIndicator
import com.vajnar.moonstalker3.ui.screen.components.Status
import com.vajnar.moonstalker3.viewmodel.MainViewModel

@Composable
fun MainScreen(
    vm: MainViewModel,
    modifier: Modifier = Modifier
) {
    val status by vm.status.collectAsStateWithLifecycle()
    val alarm by vm.alarm.collectAsStateWithLifecycle()
    val alarmMessage by vm.alarmMessage.collectAsStateWithLifecycle()
    val infoMessage by vm.infoMessage.collectAsStateWithLifecycle()

    val (actionText, actionIcon) = when (status) {
        OpCodes.ERROR -> "Reset Error" to Icons.Default.Refresh
        OpCodes.RESET -> "Resetting..." to Icons.Default.Refresh
        OpCodes.NOT_CONNECTED -> "Connect" to Icons.Default.PlayArrow
        OpCodes.CONNECTING -> "Connecting" to Icons.Default.PlayArrow
        else -> "Action" to Icons.Default.PlayArrow
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { vm.performAction() },
                icon = { Icon(imageVector = actionIcon, contentDescription = actionText) },
                text = { Text(text = actionText) },
                containerColor = when (status) {
                    OpCodes.ERROR -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.primaryContainer
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlarmIndicator(
                    alarmLevel = alarm,
                    alarmMessage = alarmMessage
                )
            }

            Status(
                status = status,
                infoMessage = infoMessage
            )
        }
    }
}