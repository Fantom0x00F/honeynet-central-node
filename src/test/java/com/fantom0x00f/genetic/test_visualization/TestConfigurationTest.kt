package com.fantom0x00f.genetic.test_visualization

import com.fantom0x00f.genetic.TestConfigurationBuilder
import org.junit.Test
import javax.swing.JFrame

class TestConfigurationTest {

    @Test
    fun test() {
        val frame = Visual(TestConfigurationBuilder(1).getTestNetwork(30))
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(500, 500)
        frame.isVisible = true
        while (true) {
            Thread.sleep(1000)
        }
    }
}