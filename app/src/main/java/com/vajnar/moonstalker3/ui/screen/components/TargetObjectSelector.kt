package com.vajnar.moonstalker3.ui.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun TargetObjectSelector(
    objectNames: List<String>,
    selectedObjectName: String,
    onObjectSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    androidx.compose.foundation.layout.Column(modifier = modifier.fillMaxWidth()) {
        Text("Target object")
        androidx.compose.foundation.layout.Box {
            OutlinedButton(
                onClick = { expanded = true },
                enabled = objectNames.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = selectedObjectName.ifEmpty {
                        if (objectNames.isEmpty()) "No objects available" else "Select an object"
                    }
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                objectNames.forEach { name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onObjectSelected(name)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
