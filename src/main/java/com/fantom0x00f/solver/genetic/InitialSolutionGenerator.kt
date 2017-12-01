package com.fantom0x00f.solver.genetic

import com.fantom0x00f.entity.NetworkConfiguration
import java.util.*

class InitialSolutionGenerator(networkConfiguration: NetworkConfiguration) {

    private val random = Random(Long.MAX_VALUE / 20)
    private val available = networkConfiguration.nodeGroups.map { it.availableAgents.size }.toIntArray()

    fun generateSolution(problem: HoneypotProblem) = HoneypotSolution(problem,
            HoneysDistribution(available.map { random.nextInt(it + 1) }.toIntArray(), available))

}