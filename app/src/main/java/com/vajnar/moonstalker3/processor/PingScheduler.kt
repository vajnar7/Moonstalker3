package com.vajnar.moonstalker3.processor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PingScheduler(
    private val scope: CoroutineScope,
    private val intervalMs: Long = 5000L,
    private val onPing: suspend () -> Unit,
) {
    private var pingJob: Job? = null

    val isRunning: Boolean
        get() = pingJob?.isActive == true

    fun start() {
        if (isRunning) return
        pingJob = scope.launch {
            while (isActive) {
                delay(intervalMs.milliseconds)
                try {
                    onPing()
                } catch (_: Exception) {
                    // Ignore or handle errors during periodic ping
                }
            }
        }
    }

    fun stop() {
        pingJob?.cancel()
        pingJob = null
    }
}
