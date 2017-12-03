package com.fantom0x00f.solver.genetic.operators

import com.fantom0x00f.solver.genetic.HoneypotSolution
import org.uma.jmetal.problem.Problem
import org.uma.jmetal.util.evaluator.SolutionListEvaluator

class EvaluatorWithStatistics(private val backendEvaluator: SolutionListEvaluator<HoneypotSolution>) : SolutionListEvaluator<HoneypotSolution> {

    var iteration = -1

    var bestDistribution = Double.MAX_VALUE
    var bestAgenstCount = Int.MAX_VALUE
    var bestSolutionIteration = -1
    var bestByProbIteraion = -1

    override fun shutdown() {
    }

    override fun evaluate(solutionList: MutableList<HoneypotSolution>, problem: Problem<HoneypotSolution>): MutableList<HoneypotSolution> {
        iteration++
        val evaluated = backendEvaluator.evaluate(solutionList, problem)

        val solutions = ArrayList(solutionList)
        val bestSolution: HoneypotSolution = solutions.minWith(Comparator { a, b ->
            var r = java.lang.Double.compare(a.getObjective(0), b.getObjective(0))
            if (r != 0) return@Comparator r
//            r = java.lang.Double.compare(a.getObjective(1), b.getObjective(1))
//            if (r != 0) return@Comparator r
            return@Comparator 0
        })!!
        if (bestSolution.getObjective(1) < bestDistribution) {
            bestDistribution = bestSolution.getObjective(1)
            bestAgenstCount = bestSolution.getObjective(2).toInt()
            bestSolutionIteration = iteration
            bestByProbIteraion = iteration
        } else if (bestSolution.getObjective(1) == bestDistribution && bestSolution.getObjective(2) < bestAgenstCount) {
            bestAgenstCount = bestSolution.getObjective(2).toInt()
            bestSolutionIteration = iteration
        }
        println("${bestSolution.getObjective(0)}")
        return evaluated
    }

}