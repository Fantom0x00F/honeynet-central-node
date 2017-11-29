package com.fantom0x00f.network.impls

import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.NetworkService
import org.springframework.stereotype.Service

@Service
class NetworkServiceImpl : NetworkService {

    private var groupsCounter: Int = 0
    private val availableGroups: MutableList<NodeGroup> = mutableListOf()
    private var inputNode: Int = -1
    private val vulnerableNodes: MutableSet<Int> = mutableSetOf()

    override fun createNodeGroup(groupName: String): NodeGroup =
            NodeGroup(groupsCounter++, groupName).apply {
                availableGroups.add(this)
            }

    override fun getNodeGroup(groupName: String): NodeGroup = availableGroups.find { it.name == groupName } ?: throw RuntimeException("Group not found")

    override fun getNodeGroups(): List<NodeGroup> = availableGroups

    override fun linkNodes(group: NodeGroup, vararg adjGroups: Int) {
        val filteredAdjGroups = adjGroups.filter { group.id != it && !group.adjacents.contains(it) }.toList()
        availableGroups.filter { filteredAdjGroups.contains(it.id) }.forEach {
            group.adjacents.add(it.id)
            it.adjacents.add(group.id)
        }
    }

    override fun getNetworkConfiguration(): NetworkConfiguration =
            NetworkConfiguration(availableGroups, inputNode, vulnerableNodes.toList())

    override fun makeVulnerable(nodeGroupId: Int) {
        vulnerableNodes.add(nodeGroupId)
    }

    override fun setInputNode(nodeGroupId: Int) {
        inputNode = nodeGroupId
    }
}