package com.fantom0x00f.solver.genetic

import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.solver.getProbability
import org.uma.jmetal.problem.Problem

class HoneypotProblem(private val networkConfiguration: NetworkConfiguration, private val initialGenerator: InitialSolutionGenerator) : Problem<HoneypotSolution> {

    override fun getName(): String = "HoneypotProblem"

    override fun evaluate(solution: HoneypotSolution) {
        solution.setObjective(0, 1 - getProbability(networkConfiguration, solution.honeysDistribution))
        solution.setObjective(1, solution.honeysDistribution.enables.sum().toDouble())
    }

    override fun getNumberOfObjectives(): Int = 2

    override fun getNumberOfVariables(): Int = 0

    override fun getNumberOfConstraints(): Int = 0

    override fun createSolution(): HoneypotSolution = initialGenerator.generateSolution(this)

}
