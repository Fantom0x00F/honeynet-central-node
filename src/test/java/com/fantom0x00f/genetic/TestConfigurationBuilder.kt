package com.fantom0x00f.genetic

import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.impls.NetworkServiceImpl
import java.util.*

class TestConfigurationBuilder(val countForSearch: Int) {

    fun getTestNetwork(groupsCount: Int): List<NodeGroup> {
        val networkService = NetworkServiceImpl()
        val result = mutableListOf<NodeGroup>()
        val random = Random()
        for (i in 0..groupsCount) {
            val newGroup = networkService.createNodeGroup("Nodes $i")
            result.add(newGroup)
            val linkedGroups = random.nextInt(countForSearch)

            if (result.isNotEmpty()) {
                networkService.linkNodes(newGroup, *((0..linkedGroups).map { result[random.nextInt(result.size)].id }.toIntArray()))
            }
        }
        return result
    }
}