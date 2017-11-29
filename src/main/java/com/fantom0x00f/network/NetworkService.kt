package com.fantom0x00f.network

import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.entity.NodeGroup

interface NetworkService {

    fun createNodeGroup(groupName: String): NodeGroup
    fun getNodeGroup(groupName: String): NodeGroup
    fun getNodeGroups(): List<NodeGroup>
    fun getNetworkConfiguration(): NetworkConfiguration
    fun makeVulnerable(nodeGroupId: Int)
    fun setInputNode(nodeGroupId: Int)
    fun linkNodes(group: NodeGroup, vararg adjGroups: Int)

}