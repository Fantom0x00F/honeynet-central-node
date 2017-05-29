package com.fantom0x00f.entity

import org.springframework.web.socket.WebSocketSession

class Agent {
    var name: String = ""
    var location: String = ""
    var containerName: String = ""
    var secret: String = ""
    var responseSecret: String = ""
    var establishedSession: WebSocketSession? = null
}