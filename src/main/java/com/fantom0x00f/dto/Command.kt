package com.fantom0x00f.dto

class CommandTypes {
    companion object {
        val GetConfiguration: Int = 101
        val SetConfiguration: Int = 102
        val StartContainer: Int = 301
        val StopContainer: Int = 302
    }
}

class EventTypes {
    companion object {
        val ReturnConfiguration: Int = 101
        val ContainerStarted: Int = 301
        val ContainerStopped: Int = 302
        val MotionDetected: Int = 500
    }
}

data class Command(
        var Type: Int? = null,
        var Message: String? = null
)

data class Event(
        var Type: Int? = null,
        var Message: String? = null
)

