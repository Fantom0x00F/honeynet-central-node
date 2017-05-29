package com.fantom0x00f.agents.implementations

import com.fantom0x00f.agents.AgentService
import com.fantom0x00f.dto.Command
import com.fantom0x00f.dto.Event
import com.fantom0x00f.echo.EchoWSHandler
import com.fantom0x00f.entity.Agent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.Arrays.asList
import javax.annotation.PostConstruct

@Service
open class AgentServiceImpl : AgentService {

    @Autowired
    private lateinit var jacksonObjectMapper: ObjectMapper

    @Autowired
    private lateinit var wsHandler: EchoWSHandler

    private val agents = getAllAgents()

    private val eventsCallbacks = ArrayList<(event: Event, agentName: String) -> Unit>()
    private val failCallbacks = ArrayList<(agentName: String) -> Unit>()
    private val connectCallbacks = ArrayList<(agentName: String) -> Unit>()

    @PostConstruct
    fun initialize() {
        wsHandler.subscribeOnMessages { message, session -> onMessage(session, message) }
        wsHandler.subscribeOnConnectionClose { session -> onCloseConnection(session) }
    }

    override fun getAgents(): List<Agent> = agents

    override fun sendCommand(command: Command, agentName: String) {
        agents.find { it.name == agentName }?.apply {
            if (establishedSession != null) {
                establishedSession!!.sendMessage(TextMessage(jacksonObjectMapper.writeValueAsString(command)))
            }
        }
    }

    override fun subscribeOnEvents(callback: (event: Event, agentName: String) -> Unit) {
        eventsCallbacks.add(callback)
    }

    override fun subscribeOnConnect(callback: (agentName: String) -> Unit) {
        connectCallbacks.add(callback)
    }

    override fun subscribeOnConnectionFail(callback: (agentName: String) -> Unit) {
        failCallbacks.add(callback)
    }

    private fun onAgentConnected(agent: Agent) {
        connectCallbacks.forEach { it.invoke(agent.name) }
    }

    private fun onEvent(event: Event, agent: Agent) {
        eventsCallbacks.forEach { it.invoke(event, agent.name) }
    }

    private fun onConnectionFail(agent: Agent) {
        failCallbacks.forEach { it.invoke(agent.name) }
    }

    private fun onMessage(session: WebSocketSession, message: String) {
        if (!verifyConnection(session, message)) {
            session.close()
            return
        }
        val agent = agents.find { it.establishedSession == session }
        if (agent == null) {
            agents.find { it.secret == message }?.apply {
                establishedSession = session
                onAgentConnected(this)
            }
        } else {
            onEvent(jacksonObjectMapper.readValue(message, Event::class.java), agent)
        }
    }

    private fun onCloseConnection(session: WebSocketSession) {
        agents.find { it.establishedSession == session }?.apply {
            establishedSession = null
            onConnectionFail(this)
        }
    }

    private fun verifyConnection(session: WebSocketSession, message: String): Boolean {
        val attributes = session.attributes!!
        if (attributes.containsKey("Verified")) return true
        agents.find { it.secret == message }?.apply {
            attributes["Verified"] = "true"
            session.sendMessage(TextMessage(responseSecret))
            return true
        }
        return false
    }

    private fun getAllAgents(): List<Agent> {
        return asList(Agent().apply {
            name = "first"
            location = "localhost"
            containerName = "friendlyhello"
            secret = "ownsecret"
            responseSecret = "ownsecret2"
        })
    }

}