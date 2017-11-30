package com.fantom0x00f.solver

import com.fantom0x00f.entity.NetworkConfiguration
import com.fantom0x00f.entity.NodeGroup
import java.util.*

fun getProbability(networkConfiguration: NetworkConfiguration): Double {
    val idToIndex = mutableMapOf<Int, Int>()
    val idToNode = mutableMapOf<Int, NodeGroup>()
    networkConfiguration.nodeGroups.forEachIndexed { i, nG -> idToIndex[nG.id] = i }
    networkConfiguration.nodeGroups.forEach { idToNode[it.id] = it }

    val graph: Array<List<Edge>> = networkConfiguration.nodeGroups.map { nG ->
        nG.adjacents.map { adj ->
            val to = idToNode[adj]!!
            val honeypots = to.availableAgents.filter { it.enabled }.size.toDouble()
            Edge(idToIndex[adj]!!, 1 - honeypots / (to.nodesCount + honeypots))
        }.toList()
    }.toTypedArray()

    val result = find_costs(graph, idToIndex[networkConfiguration.inputNodeId]!!)
    val mostVulnerable = networkConfiguration.vulnerableNodeIds.minBy { result[idToIndex[it]!!] }!!

    return result[idToIndex[mostVulnerable]!!]
}


private class Edge(var to: Int, var weight: Double)

private fun find_costs(adjList: Array<List<Edge>>, startNode: Int): DoubleArray {
    val result = DoubleArray(adjList.size)
    result.fill(Double.MAX_VALUE)
    result[startNode] = 0.0
    val q = PriorityQueue<Int>()
    q.add(startNode)
    while (!q.isEmpty()) {
        val a = q.remove().toInt()
        for (e in adjList[a]) {
            val d = e.to
            val znach = 1 - (1 - result[a]) * e.weight
            if (result[d] > znach) {
                result[d] = znach
                q.add(d)
            }
        }
    }
    return result
}