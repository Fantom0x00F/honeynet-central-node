package com.fantom0x00f.dto

data class Command(
        var Type: Int? = null,
    var Message: String? = null
)

data class Event(
        var Type: Int? = null,
    var Message: String? = null
)