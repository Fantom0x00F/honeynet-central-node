package com.fantom0x00f.network

import com.fantom0x00f.entity.NodeGroup

interface NetworkService {

    fun createNodeGroup(groupName: String): NodeGroup
    fun getNodeGroup(groupName: String): NodeGroup
    fun getNodeGroups(): List<NodeGroup>
    fun linkNodes(group: NodeGroup, vararg adjGroups: Int)

}