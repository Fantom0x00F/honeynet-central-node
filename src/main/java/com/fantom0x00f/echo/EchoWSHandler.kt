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

    companion object {
        val logger: Logger = LoggerFactory.getLogger(EchoWSHandler::class.java)
    }

    @Autowired
    lateinit var echoService: EchoService

    @Value("\${agent.secret.onconnection}")
    private lateinit var expectedSecret: String

    @Value("\${agent.secret.response}")
    private lateinit var responseSecret: String

    @Autowired
    lateinit var jacksonObjectMapper: ObjectMapper

    private var callbacks = ArrayList<(event: Event) -> Unit>()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage?) {
        val messagePayload = message?.payload ?: "error"
        if (!verifyConnection(session, messagePayload))
            return

        val event = jacksonObjectMapper.readValue(messagePayload, Event::class.java)
        pushEvent(event)

        val messageResponse = Message()
        messageResponse.Type = (event.Type ?: 0) + 1
        val response = echoService.getResponse(event.Message ?: "")
        messageResponse.Message = response

        val writeValueAsString = jacksonObjectMapper.writeValueAsString(messageResponse)
        logger.info("Responce: $writeValueAsString")
        session.sendMessage(TextMessage(writeValueAsString))
    }

    override fun afterConnectionEstablished(session: WebSocketSession?) {
        logger.debug("Opened new session $session")
    }

    override fun handleTransportError(session: WebSocketSession?, exception: Throwable?) {
        session?.close(CloseStatus.SERVER_ERROR)
    }

    fun subscribeOnEvents(callback: (event: Event) -> Unit) {
        callbacks.add(callback)
    }

    private fun pushEvent(event: Event) = callbacks.forEach { it.invoke(event) }

    private fun verifyConnection(session: WebSocketSession, message: String): Boolean {
        val attributes = session.attributes!!
        if (!attributes.containsKey("Verified")) {
            if (message != expectedSecret) {
                session.close()
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