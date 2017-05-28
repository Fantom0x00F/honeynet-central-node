package com.fantom0x00f.echo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Service
open class EchoWSHandler : TextWebSocketHandler() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(EchoWSHandler::class.java)
    }

    @Autowired
    lateinit var echoService: EchoService

    override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        val messagePayload = message?.payload ?: "error"
        logger.info("Received message $messagePayload")
        val response = echoService.getResponse(messagePayload)
        logger.info("Prepared response $messagePayload")
        session?.sendMessage(TextMessage(response))
    }

    override fun afterConnectionEstablished(session: WebSocketSession?) {
        logger.debug("Opened new session $session")
    }

    override fun handleTransportError(session: WebSocketSession?, exception: Throwable?) {
        session?.close(CloseStatus.SERVER_ERROR)
    }
}