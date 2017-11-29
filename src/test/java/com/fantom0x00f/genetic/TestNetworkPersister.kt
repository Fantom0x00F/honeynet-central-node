package com.fantom0x00f.genetic

import com.fantom0x00f.entity.Agent
import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.impls.NetworkServiceImpl
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*

fun persist(network: List<NodeGroup>, fileName: String) {
    val out = PrintWriter(FileWriter(fileName))
    out.println(network.size)
    network.sortedBy { it.id }.forEach { node ->
        val joiner = StringJoiner(";")
        joiner.add(node.id.toString())
        joiner.add(node.name)
        joiner.add(node.availableAgents.size.toString())
        joiner.add(node.availableAgents.filter { it.enabled }.size.toString())
        out.println(joiner.toString())
    }
    network.sortedBy { it.id }.forEach { node ->
        val joiner = StringJoiner(";")
        node.adjacents.forEach {
            joiner.add(it.toString())
        }
        out.println(joiner.toString())
    }
    out.flush()
    out.close()
}


fun load(fileName: String): List<NodeGroup> {
    val networkService = NetworkServiceImpl()
    val result = mutableListOf<NodeGroup>()

    val reader = BufferedReader(FileReader(fileName))
    val nodesCount = Integer.valueOf(reader.readLine())
    for (i in 0 until nodesCount) {
        val values = reader.readLine().split(";")
        val newNodeGroup = networkService.createNodeGroup(values[1])
        val agentsCount = Integer.valueOf(values[2])
        val enabledAgentsCount = Integer.valueOf(values[3])

        (0 until agentsCount).forEach {
            val newAgent = Agent()
            newNodeGroup.availableAgents.add(newAgent)
            newAgent.enabled = it < enabledAgentsCount
        }
        result.add(newNodeGroup)
    }
    for (i in 0 until nodesCount) {
        networkService.linkNodes(result[i], *reader.readLine().split(";").map { Integer.parseInt(it) }.toIntArray())
    }
    return result
}