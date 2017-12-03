package com.fantom0x00f.solver.genetic

import com.fantom0x00f.entity.NetworkConfiguration

class HoneysDistribution(val enables: IntArray, val available: IntArray) {

    fun apply(networkConfiguration: NetworkConfiguration) {
        networkConfiguration.nodeGroups.forEachIndexed { index, nG -> nG.availableAgents.forEachIndexed { i, agent -> agent.enabled = (i < enables[index]) } }
    }
}