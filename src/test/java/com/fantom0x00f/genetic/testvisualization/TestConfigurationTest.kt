package com.fantom0x00f.genetic.testvisualization

import com.fantom0x00f.entity.Agent
import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.genetic.TestConfigurationBuilder
import com.fantom0x00f.solver.DistributionSolver
import com.fantom0x00f.solver.getProbability
import org.junit.Test
import javax.swing.JFrame

class TestConfigurationTest {

    @Test
    fun test() {
        val testNetwork = TestConfigurationBuilder(25).getLayeredNetwork(10, 3)
        var totalAgents = 0
        testNetwork.nodeGroups.forEach { nG ->
            val agentsCount = nG.nodesCount * 20 / 100
            totalAgents += agentsCount
            (1..agentsCount).forEach {
                val newAgent = Agent()
                nG.availableAgents.add(newAgent)
                newAgent.enabled = true
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
    }

//    @Test
//    fun brute() {
//        val out1 = PrintWriter(FileWriter("diagramm_by_prob"))
//        val out2 = PrintWriter(FileWriter("diagramm_by_all"))
//
//        var i = 0
//        while (i < 1000) {
//            val testNetwork = TestConfigurationBuilder(1, 25).getLayeredNetwork(7, 3)
//            enableAllAgents(testNetwork)
//            val initialProbability = getProbability(testNetwork)
//            if (initialProbability > 1) {
//                continue
//            }
//
//            val (probability, bestByProbIteration, iteration) = DistributionSolver.solve(testNetwork)
//            if (probability != initialProbability) {
//                System.err.println(" Не найден оптимум")
//                persist(testNetwork, "test_cases/bas${ThreadLocalRandom.current().nextDouble()}")
//                continue
//            }
//            out1.println(bestByProbIteration)
//            out2.println(iteration)
//            out1.flush()
//            out2.flush()
//            i++
//            println("Solved $i on iteration $bestByProbIteration/$iteration")
//        }
//
//        out1.flush()
//        out2.flush()
//        out1.close()
//        out2.close()
//    }

    private fun enableAllAgents(networkConfiguration: NetworkConfiguration) {
        networkConfiguration.nodeGroups.forEach { nG ->
            val agentsCount = nG.nodesCount * 20 / 100
            nG.availableAgents.clear()
            (1..agentsCount).forEach {
                val newAgent = Agent()
                nG.availableAgents.add(newAgent)
                newAgent.enabled = true
            }
        }
    }

    fun view(networkConfiguration: NetworkConfiguration) {
        val frame = Visual(networkConfiguration)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(700, 700)
        frame.isVisible = true
        while (true) {
            Thread.sleep(1000)
        }
    }
}