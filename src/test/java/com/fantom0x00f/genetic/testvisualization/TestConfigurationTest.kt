package com.fantom0x00f.genetic.testvisualization

import com.fantom0x00f.entity.Agent
import com.fantom0x00f.genetic.load
import com.fantom0x00f.solver.getProbability
import org.junit.Test

class TestConfigurationTest {

    @Test
    fun test() {
//        val testNetwork = TestConfigurationBuilder(1, 25).getTestNetwork(30)
        val testNetwork = load("test_data/case1")
        testNetwork.nodeGroups.forEach { nG ->
            val agentsCount = nG.nodesCount * 20 / 100
            (1..agentsCount).forEach {
                val newAgent = Agent()
                nG.availableAgents.add(newAgent)
                newAgent.enabled = true
//                newAgent.enabled = Math.random() > 0.5
            }
        }
        println(getProbability(testNetwork))

//        val frame = Visual(testNetwork)
//        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//        frame.setSize(500, 500)
//        frame.isVisible = true
//        persist(testNetwork, "variant1")
//        while (true) {
//            Thread.sleep(1000)
//        }
    }
}