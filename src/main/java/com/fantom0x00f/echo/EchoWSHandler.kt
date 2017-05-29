package com.fantom0x00f.echo

import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Service
open class EchoWSHandler : TextWebSocketHandler() {

    private var callbacks = ArrayList<(message: String, session: WebSocketSession) -> Unit>()
    private var callbacksForConnectionFails = ArrayList<(session: WebSocketSession) -> Unit>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage?) {
        val messagePayload = message?.payload ?: "error"
        callbacks.forEach { it.invoke(messagePayload, session) }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus?) {
        callbacksForConnectionFails.forEach { it.invoke(session) }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable?) {
        session.close(CloseStatus.SERVER_ERROR)
    }

    fun subscribeOnMessages(callback: (message: String, session: WebSocketSession) -> Unit) {
        callbacks.add(callback)
    }

    fun subscribeOnConnectionClose(callback: (session: WebSocketSession) -> Unit) {
        callbacksForConnectionFails.add(callback)
    }

}