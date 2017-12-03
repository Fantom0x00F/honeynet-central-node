package com.fantom0x00f.entity

data class NetworkConfiguration(
        val nodeGroups: List<NodeGroup>,
        val inputNodeId: Int,
        val vulnerableNodeIds: List<Int>
)