package com.fantom0x00f.genetic

import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.impls.NetworkServiceImpl
import java.util.*

class TestConfigurationBuilder(private val countForSearch: Int) {

    fun getTestNetwork(groupsCount: Int): List<NodeGroup> {
        val networkService = NetworkServiceImpl()
        val result = mutableListOf<NodeGroup>()
        val random = Random()
        for (i in 0..groupsCount) {
            val newGroup = networkService.createNodeGroup("Nodes $i")
            result.add(newGroup)
            val linkedGroups = random.nextInt(countForSearch)

            if (result.size > 1) {
                val adjGroups = (0..linkedGroups).map { result[random.nextInt(result.size - 1)].id }.toIntArray()
                assert(!adjGroups.contains(newGroup.id))
                networkService.linkNodes(newGroup, *adjGroups)
            }
        }
        return result
    }
}