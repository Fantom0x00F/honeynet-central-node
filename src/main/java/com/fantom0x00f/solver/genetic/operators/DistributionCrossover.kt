package com.fantom0x00f.solver.genetic.operators

import com.fantom0x00f.solver.genetic.HoneypotSolution
import com.fantom0x00f.solver.genetic.HoneysDistribution
import org.uma.jmetal.operator.CrossoverOperator
import java.util.*

class DistributionCrossover : CrossoverOperator<HoneypotSolution> {
    private val random = Random(3245123455624)

    override fun execute(source: MutableList<HoneypotSolution>): MutableList<HoneypotSolution> {
        val available = source[0].honeysDistribution.available
        val distribution1 = source[0].honeysDistribution.enables
        val distribution2 = source[1].honeysDistribution.enables
        val size = distribution1.size

        return (1..3).map {
            val resultDistribution = IntArray(size)
            for (i in 0 until size) {
                if (distribution1[i] == distribution2[i]) {
                    resultDistribution[i] = distribution1[i]
                } else {
                    val abs = Math.abs(distribution1[i] - distribution2[i])
                    val middle = Math.min(distribution1[i], distribution2[i]) + abs / 2
                    val sign = if (random.nextDouble() > 0.5) 1 else -1
                    val value = Math.max(0, middle + sign * random.nextInt(abs))
                    resultDistribution[i] = Math.min(value, available[i])
                }
            }
            HoneypotSolution(source[0].problem, HoneysDistribution(resultDistribution, available))
        }.toMutableList()
    }

    override fun getNumberOfParents(): Int = 2
}