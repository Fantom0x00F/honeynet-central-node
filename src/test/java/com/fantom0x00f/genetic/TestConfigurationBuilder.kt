package com.fantom0x00f.genetic

import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.impls.NetworkServiceImpl
import java.util.*

class TestConfigurationBuilder(private val thresholdNodes: Int) {

    fun getLayeredNetwork(groupsInLayerThreshold: Int, layersCount: Int): NetworkConfiguration {
        val networkService = NetworkServiceImpl()
        val result = (1..layersCount).map { mutableListOf<NodeGroup>() }.toList()
        val random = Random()

        val inputNode = networkService.createNodeGroup("Input Node")
        networkService.setInputNode(inputNode.id)
        inputNode.nodesCount = 1

        for (i in 0 until layersCount) {
            val ngOfLayer = result[i]

            for (j in 0 until groupsInLayerThreshold) {
                val newGroup = networkService.createNodeGroup("Group $i Node $j")
                ngOfLayer.add(newGroup)
                val linkedGroups = 1

                if (ngOfLayer.size > 1) {
                    val adjGroups = (0..linkedGroups).map { ngOfLayer[random.nextInt(ngOfLayer.size - 1)].id }.toIntArray()
                    assert(!adjGroups.contains(newGroup.id))
                    networkService.linkNodes(newGroup, *adjGroups)
                }
            }
            ngOfLayer.forEach { it.nodesCount = random.nextInt(thresholdNodes * it.adjacents.size - 1) + 1 }
        }
        result[0].forEach { networkService.linkNodes(it, inputNode.id) }

        for (i in 0 until layersCount - 1) {
            val from = result[i]
            val to = result[i + 1]
            from.filter { random.nextDouble() > 0.6 }.forEach { fromNg ->
                networkService.linkNodes(fromNg, to[random.nextInt(to.size)].id)
                if (random.nextDouble() > 0.95) {
                    networkService.linkNodes(fromNg, to[random.nextInt(to.size)].id)
                }
            }
        }
        networkService.makeVulnerable(result.last()[random.nextInt(result.last().size)].id)
        return networkService.getNetworkConfiguration()
    }
}