package com.fantom0x00f.ui

import com.fantom0x00f.dto.*
import com.fantom0x00f.echo.EchoWSHandler
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
    lateinit var wsHandler: EchoWSHandler

    @Autowired
    private lateinit var jacksonObjectMapper: ObjectMapper

    var textArea = TextArea("")

    lateinit var imageName: TextField
    lateinit var startparams: TextField

    override fun init(request: VaadinRequest) {

        textArea.setWidth(100.0f, Sizeable.Unit.PERCENTAGE)

        content = VerticalLayout(textArea, HorizontalLayout(
                buildCommandsPanel(),
                buildConfigurationPanel()
        ))
        wsHandler.subscribeOnEvents(this::receiveEvent)
    }

    fun buildCommandsPanel(): VerticalLayout {
        val id = TextField("CommandID")
        val name = TextField("Command")

        val startContainerButton = Button("Start container")
        val stopContainerButton = Button("Stop container")
        val greetButton = Button("Send command")

        startContainerButton.addClickListener { startContainer() }
        stopContainerButton.addClickListener { stopContainer() }

        greetButton.addClickListener {
            wsHandler.sendCommand(Command(Integer.parseInt(id.value), name.value))
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
            wsHandler.sendCommand(Command(CommandTypes.GetConfiguration, ""))
        }

        setConfigurationButton.addClickListener {
            wsHandler.sendCommand(Command(CommandTypes.SetConfiguration,
                    jacksonObjectMapper.writeValueAsString(WorkerConfiguration(imageName.value, startparams.value))
            ))
        }

        return VerticalLayout(imageName, startparams, getConfigurationButton, setConfigurationButton)
    }

    fun receiveEvent(event: Event) {
        access {
            if (event.Type == EventTypes.ReturnConfiguration) {
                parseConfiguration(event.Message!!)
            }
            textArea.value += "\n" + event.toString()
            textArea.setCursorPosition(textArea.value.length);
        }
    }

    fun startContainer() {
        wsHandler.sendCommand(Command(CommandTypes.StartContainer, ""))
    }

    fun stopContainer() {
        wsHandler.sendCommand(Command(CommandTypes.StopContainer, ""))
    }

    private fun parseConfiguration(config: String) {
        println("RECEIVED $config")
        val (ImageName, RunParameters) = jacksonObjectMapper.readValue(config, WorkerConfiguration::class.java)
        imageName.value = ImageName
        startparams.value = RunParameters
    }

}