package com.fantom0x00f.echo

import com.fantom0x00f.dto.Event
import com.fantom0x00f.dto.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Service
open class EchoWSHandler : TextWebSocketHandler() {

    private val logger: Logger = LoggerFactory.getLogger(EchoWSHandler::class.java)

    private val establishedConnections = ArrayList<WebSocketSession>()

    @Autowired
    private lateinit var echoService: EchoService

    @Value("\${agent.secret.onconnection}")
    private lateinit var expectedSecret: String

    @Value("\${agent.secret.response}")
    private lateinit var responseSecret: String

    @Autowired
    private lateinit var jacksonObjectMapper: ObjectMapper

    private var callbacks = ArrayList<(event: Event) -> Unit>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage?) {
        val messagePayload = message?.payload ?: "error"
        if (!verifyConnection(session, messagePayload))
            return

        val event = jacksonObjectMapper.readValue(messagePayload, Event::class.java)
        pushEvent(event)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.debug("Opened new session $session")
        establishedConnections.add(session)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable?) {
        session.close(CloseStatus.SERVER_ERROR)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus?) {
        logger.debug("Close session $session")
        establishedConnections.remove(session)
    }

    fun subscribeOnEvents(callback: (event: Event) -> Unit) {
        callbacks.add(callback)
    }

    fun sendMessage(message: Message) {
        if (establishedConnections.size == 1) {
            val writeValueAsString = jacksonObjectMapper.writeValueAsString(message)
            logger.info("Send message: $writeValueAsString")

            establishedConnections[0].sendMessage(TextMessage(writeValueAsString))
        } else {
            logger.info("Can't send message, because connection size = ${establishedConnections.size}")
        }
    }

    private fun pushEvent(event: Event) = callbacks.forEach { it.invoke(event) }

    private fun verifyConnection(session: WebSocketSession, message: String): Boolean {
        val attributes = session.attributes!!
        if (!attributes.containsKey("Verified")) {
            if (message != expectedSecret) {
                session.close()
                establishedConnections.remove(session)
                logger.error("Bad secret")
                return false
            }
            attributes["Verified"] = "true"
            session.sendMessage(TextMessage(responseSecret))
            return false
        }
        return true
    }
}