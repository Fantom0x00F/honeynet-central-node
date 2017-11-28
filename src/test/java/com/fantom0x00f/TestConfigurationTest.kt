package com.fantom0x00f

import com.fantom0x00f.genetic.TestConfigurationBuilder
import org.junit.Test

class TestConfigurationTest {

    @Test
    fun test() {
        val testNetwork = TestConfigurationBuilder(2).getTestNetwork(30)
        println(testNetwork)
    }
}