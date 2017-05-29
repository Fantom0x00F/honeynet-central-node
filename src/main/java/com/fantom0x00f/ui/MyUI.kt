package com.fantom0x00f.ui

import com.fantom0x00f.agents.AgentService
import com.fantom0x00f.dto.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.vaadin.annotations.Push
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired

@SpringUI
@Theme("global")
@Title("PizzaMaker")
@Push
open class MyUI : UI() {

    @Autowired
    lateinit var agentService: AgentService

    @Autowired
    private lateinit var jacksonObjectMapper: ObjectMapper

    var textArea = TextArea("")

    lateinit var imageName: TextField
    lateinit var startparams: TextField

    override fun init(request: VaadinRequest) {

        textArea.setWidth(100.0f, Sizeable.Unit.PERCENTAGE)

        content = VerticalLayout(textArea, buildAgentsList())
        agentService.subscribeOnEvents(this::receiveEvent)
    }

    fun buildAgentsList(): VerticalLayout {
        val verticalLayout = VerticalLayout()
        for (agent in agentService.getAgents()) {
            val agentPanel = Panel("").apply {
                setSizeUndefined()
                caption = "${agent.name} / ${agent.containerName}"
            }
            agentPanel.content = HorizontalLayout().apply {
                val startContainerButton = Button("Start container")
                val stopContainerButton = Button("Stop container")

                startContainerButton.addClickListener { startContainer(agent.name) }
                stopContainerButton.addClickListener { stopContainer(agent.name) }

                val container = TextField("Container")
                val setConfigurationButton = Button("Set container")
                setConfigurationButton.addClickListener {
                    agentService.sendCommand(Command(CommandTypes.SetConfiguration,
                            jacksonObjectMapper.writeValueAsString(WorkerConfiguration(container.value, ""))), agent.name)
                }

                addComponent(startContainerButton)
                addComponent(stopContainerButton)
                addComponent(container)
                addComponent(setConfigurationButton)
            }

            verticalLayout.addComponentsAndExpand(agentPanel)
        }
        return verticalLayout
    }

    fun buildCommandsPanel(): VerticalLayout {
        val id = TextField("CommandID")
        val name = TextField("Command")

        val startContainerButton = Button("Start container")
        val stopContainerButton = Button("Stop container")
        val greetButton = Button("Send command")

//        startContainerButton.addClickListener { startContainer() }
//        stopContainerButton.addClickListener { stopContainer() }

        greetButton.addClickListener {
            //            wsHandler.sendCommand(Command(Integer.parseInt(id.value), name.value))
            Notification.show("Command sended ")
        }
        return VerticalLayout(id, name, greetButton, HorizontalLayout(startContainerButton, stopContainerButton))
    }

    fun buildConfigurationPanel(): VerticalLayout {
        imageName = TextField("Image name")
        startparams = TextField("Start params")

        val getConfigurationButton = Button("Get configuration")
        val setConfigurationButton = Button("Set configuration")

        getConfigurationButton.addClickListener {
            //            wsHandler.sendCommand(Command(CommandTypes.GetConfiguration, ""))
        }

        setConfigurationButton.addClickListener {
            //            wsHandler.sendCommand(Command(CommandTypes.SetConfiguration,
//                    jacksonObjectMapper.writeValueAsString(WorkerConfiguration(imageName.value, startparams.value))
//            ))
        }

        return VerticalLayout(imageName, startparams, getConfigurationButton, setConfigurationButton)
    }

    fun receiveEvent(event: Event, agentName: String) {
        access {
            if (event.Type == EventTypes.ReturnConfiguration) {
                parseConfiguration(event.Message!!)
            }
            textArea.value += "\n" + event.toString()
            textArea.setCursorPosition(textArea.value.length);
        }
    }

    fun startContainer(agentName: String) {
        agentService.sendCommand(Command(CommandTypes.StartContainer, ""), agentName)
    }

    fun stopContainer(agentName: String) {
        agentService.sendCommand(Command(CommandTypes.StopContainer, ""), agentName)
    }

    private fun parseConfiguration(config: String) {
        println("RECEIVED $config")
        val (ImageName, RunParameters) = jacksonObjectMapper.readValue(config, WorkerConfiguration::class.java)
        imageName.value = ImageName
        startparams.value = RunParameters
    }

}