package com.fantom0x00f.genetic.testvisualization

import com.fantom0x00f.entity.Agent
import com.fantom0x00f.genetic.load
import com.fantom0x00f.solver.DistributionSolver
import com.fantom0x00f.solver.getProbability
import org.junit.Test

class TestConfigurationTest {

    @Test
    fun test() {
//        val testNetwork = TestConfigurationBuilder(1, 25).getTestNetwork(30)
        val testNetwork = load("test_data/case3")
        var totalAgents = 0
        testNetwork.nodeGroups.forEach { nG ->
            val agentsCount = nG.nodesCount * 20 / 100
            totalAgents += agentsCount
            (1..agentsCount).forEach {
                val newAgent = Agent()
                nG.availableAgents.add(newAgent)
                newAgent.enabled = true
//                newAgent.enabled = Math.random() > 0.5
            }
        }
        println("Вероятность попадания: " + getProbability(testNetwork))
        println("Использовано агентов: $totalAgents")
        val start = System.currentTimeMillis()
        DistributionSolver.solve(testNetwork)
        println()
        println("" + ((System.currentTimeMillis() - start) / 1000) + "s")
        println("Решение:")
        println("Вероятность попадания: " + getProbability(testNetwork))
        println("Использовано агентов: " + testNetwork.nodeGroups.sumBy { it.availableAgents.filter { it.enabled }.count() })

//        val frame = Visual(testNetwork)
//        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//        frame.setSize(500, 500)
//        frame.isVisible = true
//        persist(testNetwork, "variant1")
//        while (true) {
//            Thread.sleep(1000)
//        }
    }

//    fun perebot(networkConfiguration: NetworkConfiguration) {
//        networkConfiguration.nodeGroups.forEach { nG ->
//            nG.availableAgents[0].enabled = false
//            println("Вероятность попадания: " + getProbability(networkConfiguration))
//            nG.availableAgents[0].enabled = true
//        }
//    }
}