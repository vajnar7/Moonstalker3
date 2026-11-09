package com.vajnar.moonstalker3.processor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Processor(val scope: CoroutineScope) {
    private val ioActions = mutableMapOf<Int, suspend () -> Unit>()
    private val uxActions = mutableMapOf<Int, () -> Unit>()

    fun registerIo(id: Int, action: suspend () -> Unit) {
        ioActions[id] = action
    }

    fun registerUx(id: Int, action: () -> Unit) {
        uxActions[id] = action
    }

    fun execute(id: Int) {
        val ioTaskBlock = ioActions[id]
        if (ioTaskBlock != null) {
            scope.launch {ioTaskBlock()}
        }
        val uxTaskBlock = uxActions[id]
        if (uxTaskBlock != null) {
            uxTaskBlock()
        }
    }
}