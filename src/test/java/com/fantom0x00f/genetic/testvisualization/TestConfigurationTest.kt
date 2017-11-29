package com.fantom0x00f.genetic.testvisualization

import com.fantom0x00f.genetic.TestConfigurationBuilder
import com.fantom0x00f.genetic.persist
import org.junit.Test
import javax.swing.JFrame

class TestConfigurationTest {

    @Test
    fun test() {
        val testNetwork = TestConfigurationBuilder(1, 25).getTestNetwork(30)
//        val testNetwork = load("variant1")
        val frame = Visual(testNetwork)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(500, 500)
        frame.isVisible = true
        persist(testNetwork, "variant1")
        while (true) {
            Thread.sleep(1000)
        }
    }
}