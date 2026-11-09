package com.vajnar.moonstalker3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vajnar.moonstalker3.processor.Commands
import com.vajnar.moonstalker3.processor.OpCodes
import com.vajnar.moonstalker3.processor.Processor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _status = MutableStateFlow(OpCodes.NOT_CONNECTED)
    private val _alarm = MutableStateFlow(0) // 0: No alarm (Green), 1: Warning (Yellow), 2: Critical (Red)
    private val _alarmMessage = MutableStateFlow("No alarm")
    private val _infoMessage = MutableStateFlow("Idle")

    val status: StateFlow<Int> = _status.asStateFlow()
    val alarm: StateFlow<Int> = _alarm.asStateFlow()
    val alarmMessage: StateFlow<String> = _alarmMessage.asStateFlow()
    val infoMessage: StateFlow<String> = _infoMessage.asStateFlow()

    val processor = Processor(viewModelScope)

    init {
        processor.registerIo(OpCodes.RESET) {
            Commands.cmdReset()
        }
        processor.registerUx(OpCodes.RESET) {
            _infoMessage.value = "resetting"
        }

        processor.registerUx(OpCodes.ERROR) {
            _status.value = OpCodes.ERROR
            _alarm.value = 2
            _alarmMessage.value = "0"
        }

        processor.registerIo(OpCodes.CONNECT) {
//            Commands.cmdConnect()
            _status.value = OpCodes.CONNECTING
        }
    }

    fun performAction() {
        when (_status.value) {
            OpCodes.ERROR -> {
                _status.value = OpCodes.NOT_CONNECTED
                _alarm.value = 0
                _alarmMessage.value = "No alarm"
                _infoMessage.value = "Cleared error"
                processor.execute(OpCodes.RESET)
            }
            OpCodes.NOT_CONNECTED -> {
                processor.execute(OpCodes.CONNECT)
            }
            OpCodes.RESET -> {
                processor.execute(OpCodes.RESET)
            }
            else -> {
                processor.execute(OpCodes.RESET)
            }
        }
    }
}