package com.fantom0x00f.solver.genetic.operators

import com.fantom0x00f.solver.genetic.HoneypotSolution
import org.uma.jmetal.operator.MutationOperator
import java.util.*

class Mutation : MutationOperator<HoneypotSolution> {
    private val random = Random(5432567164)

    override fun execute(source: HoneypotSolution): HoneypotSolution {
        val available = source.honeysDistribution.available
        val enables = source.honeysDistribution.enables
        enables.forEachIndexed { i, value ->
            if (random.nextDouble() > 0.5) {
                val nVal = value + (random.nextInt(3) - 1) * random.nextInt(3)
                enables[i] = Math.min(Math.max(0, nVal), available[i])
                assert(enables[i] <= available[i])
                assert(enables[i] >= 0)
            }
        }
        return source
    }
}