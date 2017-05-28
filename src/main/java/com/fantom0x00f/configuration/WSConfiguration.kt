package com.fantom0x00f.configuration

import com.fantom0x00f.echo.EchoWSHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
open class WSConfiguration : WebSocketConfigurer {

    @Autowired
    lateinit var echoHandler: EchoWSHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(echoHandler, "/echo")
    }

}