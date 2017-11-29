package com.fantom0x00f.genetic

import com.fantom0x00f.entity.Agent
import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.entity.NodeGroup
import com.fantom0x00f.network.impls.NetworkServiceImpl
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*

fun persist(network: NetworkConfiguration, fileName: String) {
    val out = PrintWriter(FileWriter(fileName))
    val nodeGroups = network.nodeGroups
    out.println(nodeGroups.size)
    nodeGroups.sortedBy { it.id }.forEach { node ->
        val joiner = StringJoiner(";")
        joiner.add(node.id.toString())
        joiner.add(node.name)
        joiner.add(node.nodesCount.toString())
        joiner.add(node.availableAgents.size.toString())
        joiner.add(node.availableAgents.filter { it.enabled }.size.toString())
        out.println(joiner.toString())
    }
    out.println("Input=${network.inputNodeId}")
    val vJoiner = StringJoiner(";")
    network.vulnerableNodeIds.forEach { vJoiner.add(it.toString()) }
    out.println("Vulnerable=$vJoiner")

    nodeGroups.sortedBy { it.id }.forEach { node ->
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
        newNodeGroup.nodesCount = Integer.valueOf(values[2])

        val agentsCount = Integer.valueOf(values[3])
        val enabledAgentsCount = Integer.valueOf(values[4])
        (0 until agentsCount).forEach {
            val newAgent = Agent()
            newNodeGroup.availableAgents.add(newAgent)
            newAgent.enabled = it < enabledAgentsCount
        }
        result.add(newNodeGroup)
    }
    networkService.setInputNode(Integer.parseInt(reader.readLine().split("=")[1]))
    reader.readLine().split("=")[1].split(";").forEach {
        networkService.makeVulnerable(Integer.parseInt(it))
    }
    for (i in 0 until nodesCount) {
        networkService.linkNodes(result[i], *reader.readLine().split(";").map { Integer.parseInt(it) }.toIntArray())
    }
    return result
}