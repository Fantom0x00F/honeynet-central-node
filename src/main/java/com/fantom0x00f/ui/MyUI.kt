package com.fantom0x00f.ui

import com.fantom0x00f.dto.Event
import com.fantom0x00f.dto.Message
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
        val name = TextField("Name")
        val greetButton = Button("Greet")

        greetButton.addClickListener {
            wsHandler.sendMessage(Message(3, name.value))
            Notification.show("Message sended ")
        }
        textArea.setWidth(100.0f, Sizeable.Unit.PERCENTAGE)
        content = VerticalLayout(textArea, name, greetButton)
        wsHandler.subscribeOnEvents(this::receiveEvent)
    }

    fun receiveEvent(event: Event) {
        access {
            textArea.value += "\n" + event.toString()
            textArea.setCursorPosition(textArea.value.length);
        }
    }

}