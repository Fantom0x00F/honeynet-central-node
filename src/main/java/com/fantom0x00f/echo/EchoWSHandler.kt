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

    override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        val messagePayload = message?.payload ?: "error"
        val attributes = session?.attributes!!
        if (!attributes.containsKey("Verified")) {
            if (messagePayload != expectedSecret) {
                session.close()
                logger.error("Bad secret")
                return
            }
            attributes["Verified"] = "true"
            session.sendMessage(TextMessage(responseSecret))
            return
        }
        logger.info("Received message $messagePayload")
        val event = jacksonObjectMapper.readValue(messagePayload, Event::class.java)
        logger.info("Received ${event.Message}")

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
}