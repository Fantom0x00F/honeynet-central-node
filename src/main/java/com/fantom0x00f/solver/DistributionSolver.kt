package com.fantom0x00f.solver

import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.solver.genetic.HoneypotProblem
import com.fantom0x00f.solver.genetic.HoneypotSolution
import com.fantom0x00f.solver.genetic.InitialSolutionGenerator
import com.fantom0x00f.solver.genetic.operators.DistributionCrossover
import com.fantom0x00f.solver.genetic.operators.Mutation
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder

object DistributionSolver {

    fun solve(networkConfiguration: NetworkConfiguration) {
        val initialSolutionGenerator = InitialSolutionGenerator(networkConfiguration)
        val honeypotProblem = HoneypotProblem(networkConfiguration, initialSolutionGenerator)
        val crossover = DistributionCrossover()
        val mutation = Mutation()


        val genetic = NSGAIIBuilder<HoneypotSolution>(honeypotProblem, crossover, mutation)
                .setPopulationSize(100)
                .setMaxEvaluations(100000)
                .build()

        genetic.run()

        var distributions = genetic.population.map { it.honeysDistribution }
                .map { Triple(1 - getProbability(networkConfiguration, it), it.enables.sum(), it) }.toList()

        distributions = distributions.sortedWith(Comparator { a, b ->
            var r = java.lang.Double.compare(a.first, b.first)
            if (r != 0) return@Comparator r
            r = java.lang.Integer.compare(a.second, b.second)
            if (r != 0) return@Comparator r
            return@Comparator 0
        })

        distributions[0].third.apply(networkConfiguration)
    }
}