package com.fantom0x00f.ui

import com.fantom0x00f.dto.Command
import com.fantom0x00f.dto.Event
import com.fantom0x00f.echo.EchoWSHandler
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

    var textArea = TextArea("")

    override fun init(request: VaadinRequest) {
        val id = TextField("CommandID")
        val name = TextField("Command")

        val greetButton = Button("Send command")

        greetButton.addClickListener {
            wsHandler.sendMessage(Command(Integer.parseInt(id.value), name.value))
            Notification.show("Command sended ")
        }
        textArea.setWidth(100.0f, Sizeable.Unit.PERCENTAGE)
        content = VerticalLayout(textArea, id, name, greetButton)
        wsHandler.subscribeOnEvents(this::receiveEvent)
    }

    fun receiveEvent(event: Event) {
        access {
            textArea.value += "\n" + event.toString()
            textArea.setCursorPosition(textArea.value.length);
        }
    }

}