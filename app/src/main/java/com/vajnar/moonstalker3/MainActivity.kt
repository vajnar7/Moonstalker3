package com.vajnar.moonstalker3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

import com.vajnar.moonstalker3.ui.screen.MainScreen
import com.vajnar.moonstalker3.ui.theme.MoonstalkerTheme
import com.vajnar.moonstalker3.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoonstalkerTheme() {
                val mainVm: MainViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        vm = mainVm,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}