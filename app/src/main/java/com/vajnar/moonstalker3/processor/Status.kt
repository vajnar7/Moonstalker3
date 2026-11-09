package com.vajnar.moonstalker3.processor

data class Status(
    var value: Int = OpCodes.NOT_CONNECTED,
    var message: String = "",
    var mode: Int = OpCodes.NOT_CONNECTED
)
