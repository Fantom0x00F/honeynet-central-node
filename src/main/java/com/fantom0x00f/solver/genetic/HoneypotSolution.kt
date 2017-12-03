package com.fantom0x00f.solver.genetic

import org.uma.jmetal.problem.Problem
import org.uma.jmetal.solution.Solution

class HoneypotSolution(val problem: Problem<HoneypotSolution>, val honeysDistribution: HoneysDistribution) : Solution<HoneysDistribution> {

    private val attributes = mutableMapOf<Any, Any>()
    private val objectives: DoubleArray = DoubleArray(problem.numberOfObjectives + 1)

    override fun getNumberOfObjectives(): Int = problem.numberOfObjectives

    override fun setObjective(index: Int, value: Double) {
        objectives[index] = value
    }

    override fun getObjective(index: Int): Double = objectives[index]

    override fun getAttribute(id: Any?): Any? = attributes[id]

    override fun setAttribute(id: Any, value: Any) {
        attributes[id] = value
    }

    override fun copy(): Solution<HoneysDistribution> = HoneypotSolution(problem, HoneysDistribution(honeysDistribution.enables.clone(), honeysDistribution.available.clone()))

    override fun getVariableValue(index: Int): HoneysDistribution {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getVariableValueString(index: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNumberOfVariables(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setVariableValue(index: Int, value: HoneysDistribution?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}