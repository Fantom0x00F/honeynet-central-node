package com.fantom0x00f.agents

import com.fantom0x00f.dto.Command
import com.fantom0x00f.dto.Event
import com.fantom0x00f.entity.Agent

interface AgentService {

    fun getAgents(): List<Agent>

    fun sendCommand(command: Command, agentName: String)

    fun subscribeOnEvents(callback: (event: Event, agentName: String) -> Unit)

    fun subscribeOnConnect(callback: (agentName: String) -> Unit)

    fun subscribeOnConnectionFail(callback: (agentName: String) -> Unit)

}