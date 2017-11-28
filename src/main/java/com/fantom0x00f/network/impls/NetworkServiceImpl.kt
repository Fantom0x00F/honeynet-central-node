package com.fantom0x00f.network.impls

import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.NetworkService
import org.springframework.stereotype.Service

@Service
class NetworkServiceImpl : NetworkService {

    private var groupsCounter: Int = 0
    private val availableGroups: MutableList<NodeGroup> = mutableListOf()

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
}