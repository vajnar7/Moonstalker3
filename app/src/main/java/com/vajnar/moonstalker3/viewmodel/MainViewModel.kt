package com.vajnar.moonstalker3.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vajnar.moonstalker3.processor.Commands
import com.vajnar.moonstalker3.processor.OpCodes
import com.vajnar.moonstalker3.processor.PingResponse
import com.vajnar.moonstalker3.processor.PingScheduler
import com.vajnar.moonstalker3.processor.Processor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _status = MutableStateFlow(OpCodes.NOT_CONNECTED)
    private val _mode = MutableStateFlow(OpCodes.NOT_CONNECTED)
    private val _alarm = MutableStateFlow(0) // 0: No alarm (Green), 1: Warning (Yellow), 2: Critical (Red)
    private val _alarmMessage = MutableStateFlow("No alarm")
    private val _infoMessage = MutableStateFlow("Idle")

    val status: StateFlow<Int> = _status.asStateFlow()
    val mode: StateFlow<Int> = _mode.asStateFlow()
    val alarm: StateFlow<Int> = _alarm.asStateFlow()
    val alarmMessage: StateFlow<String> = _alarmMessage.asStateFlow()
    val infoMessage: StateFlow<String> = _infoMessage.asStateFlow()

    val processor = Processor(viewModelScope)

    private val pingScheduler = PingScheduler(
        scope = viewModelScope,
        intervalMs = 5000L,
        onPing = {
            processor.execute(OpCodes.PING)
        }
    )

    init {
        viewModelScope.launch {
            Commands.cmdLogin("android", "password123")
        }

        processor.registerIo(OpCodes.RESET) {
            Commands.cmdReset()
        }
        processor.registerUx(OpCodes.RESET) {
            _infoMessage.value = "resetting telescope state machine"
        }

        processor.registerUx(OpCodes.ERROR) {
            _status.value = OpCodes.ERROR
            _alarm.value = 2
            _alarmMessage.value = "0"
        }

        processor.registerIo(OpCodes.CONNECT) {
            Commands.cmdConnect()
        }

        processor.registerUx(OpCodes.CONNECTING) {
            _status.value = OpCodes.CONNECTING
            _infoMessage.value = "connecting to telescope"
        }

        processor.registerIo(OpCodes.PING) {
            val res = Commands.cmdPing()
            res.onSuccess { pingResponse ->
                dispatchPing(pingResponse)
            }.onFailure { exception ->
                Log.e("MainViewModel", "Ping failed: ${exception.message}")
            }
        }

        processor.registerUx(OpCodes.READY) {
            _infoMessage.value = "ready"
            _status.value = OpCodes.READY
        }

        processor.registerUx(OpCodes.CONNECTING) {
            _infoMessage.value = "connecting to telescope"
            _status.value = OpCodes.CONNECTING
        }

        processor.registerUx(OpCodes.CONNECTED) {
            _infoMessage.value = "connected"
            _status.value = OpCodes.CONNECTED
        }

        processor.registerIo(OpCodes.MOVE_START_UP) { Commands.cmdMoveStart("up") }
        processor.registerIo(OpCodes.MOVE_START_DOWN) { Commands.cmdMoveStart("down") }
        processor.registerIo(OpCodes.MOVE_START_LEFT) { Commands.cmdMoveStart("left") }
        processor.registerIo(OpCodes.MOVE_START_RIGHT) { Commands.cmdMoveStart("right") }
        processor.registerIo(OpCodes.MOVE_END) { Commands.cmdMoveStop() }

        pingScheduler.start()
    }

    override fun onCleared() {
        pingScheduler.stop()
    }

    fun onMoveStart(direction: String) {
        viewModelScope.launch {
            _infoMessage.value = "Moving $direction..."
            Commands.cmdMoveStart(direction)
        }
    }

    fun onMoveEnd() {
        viewModelScope.launch {
            _infoMessage.value = "Move stopped"
            Commands.cmdMoveStop()
        }
    }

    fun performAction() {
        when (_status.value) {
            OpCodes.ERROR -> {
                _status.value = OpCodes.NOT_CONNECTED
                _alarm.value = 0
                _alarmMessage.value = "No alarm"
                _infoMessage.value = "Cleared error"
            }
            OpCodes.NOT_CONNECTED -> {
                processor.execute(OpCodes.CONNECT)
            }

            else -> {
//                processor.execute(OpCodes.RESET)
            }
        }
    }

    private fun dispatchPing(res: PingResponse) {
        Log.i("PEPE", "dee hat->" + res.state)
        when (res.state) {
//            "ready" -> {
//                if (_status.value != OpCodes.READY) {
//                    processor.execute(OpCodes.READY)
//                }
//            }
            "connecting" -> {
                processor.execute(OpCodes.CONNECTING)
            }
            "connected" -> {
                processor.execute(OpCodes.CONNECTED)
            }
            else -> {
                // Handle default / unknown state
            }
        }
    }
}