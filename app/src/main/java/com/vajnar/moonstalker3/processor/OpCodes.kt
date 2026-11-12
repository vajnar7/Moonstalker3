package com.vajnar.moonstalker3.processor

object OpCodes {
    const val NOT_CONNECTED = 1
    const val RESET = 2
    const val ERROR = 3
    const val CONNECT = 4
    const val CONNECTING = 5
    const val PING = 6
    const val READY = 7
    const val CONNECTED = 8

    const val MOVE_START_UP = 9
    const val MOVE_START_DOWN = 10
    const val MOVE_START_LEFT = 11
    const val MOVE_START_RIGHT = 12
    const val MOVE_END = 13
}