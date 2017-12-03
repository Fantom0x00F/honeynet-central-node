package com.fantom0x00f.genetic.testvisualization

import com.fantom0x00f.entity.NetworkConfiguration
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import javax.swing.JFrame

class Visual(networkConfiguration: NetworkConfiguration) : JFrame("Hello") {

    init {
        val graph = mxGraph()
        val parent = graph.defaultParent

        graph.model.beginUpdate()
        try {
            val nodes = networkConfiguration.nodeGroups
            val layers = listOf(
                    listOf(nodes.find { it.id == networkConfiguration.inputNodeId }!!),
                    nodes.filter { it.name.contains("Group 0") }.toList(),
                    nodes.filter { it.name.contains("Group 1") }.toList(),
                    nodes.filter { it.name.contains("Group 2") }.toList()
            )
            assert(layers.map { it.size }.sum() == nodes.size)

            val vertexes = mutableMapOf<Int, Any>()

            layers.forEachIndexed { index, list ->
                list.forEach {
                    val nodeName = if (networkConfiguration.inputNodeId == it.id) {
                        "I"
                    } else {
                        if (networkConfiguration.vulnerableNodeIds.contains(it.id)) {
                            "V"
                        } else {
                            ""
                        }
                    }
                    vertexes[it.id] = graph.insertVertex(parent, null, nodeName, Math.random() * 500, index * 200 + Math.random() * 100, 20.0, 20.0)
                }
            }


            nodes.forEach { node ->
                node.adjacents.forEach { adj ->
                    graph.insertEdge(parent, null, "", vertexes[node.id], vertexes[adj])
                }
            }
        } finally {
            graph.model.endUpdate()
        }

        val graphComponent = mxGraphComponent(graph)
        contentPane.add(graphComponent)
    }
}