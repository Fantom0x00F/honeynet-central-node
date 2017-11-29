package com.fantom0x00f.genetic

import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.impls.NetworkServiceImpl
import java.util.*

class TestConfigurationBuilder(private val countForSearch: Int, private val thresholdNodes: Int) {

    fun getTestNetwork(groupsCount: Int): NetworkConfiguration {
        val networkService = NetworkServiceImpl()
        val result = mutableListOf<NodeGroup>()
        val random = Random()
        for (i in 0 until groupsCount) {
            val newGroup = networkService.createNodeGroup("Nodes $i")
            result.add(newGroup)
            val linkedGroups = random.nextInt(countForSearch)

            if (result.size > 1) {
                val adjGroups = (0..linkedGroups).map { result[random.nextInt(result.size - 1)].id }.toIntArray()
                assert(!adjGroups.contains(newGroup.id))
                networkService.linkNodes(newGroup, *adjGroups)
            }
        }
        result.forEach { it.nodesCount = random.nextInt(thresholdNodes * it.adjacents.size) }

        networkService.setInputNode(result.minBy { nodeGroup -> nodeGroup.adjacents.size }!!.id)
        networkService.makeVulnerable(result[random.nextInt(result.size)].id)

        return networkService.getNetworkConfiguration()
    }
}