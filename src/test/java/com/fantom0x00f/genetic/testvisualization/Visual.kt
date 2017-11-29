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
            val vertexes = nodes.map {
                val nodeName = if (networkConfiguration.inputNodeId == it.id) {
                    "I"
                } else {
                    if (networkConfiguration.vulnerableNodeIds.contains(it.id)) {
                        "V"
                    } else {
                        ""
                    }
                }
                graph.insertVertex(parent, null, nodeName, Math.random() * 500, Math.random() * 500, 10.0, 10.0)
            }.toList()

            nodes.forEachIndexed { index, node ->
                node.adjacents.forEach { adj ->
                    graph.insertEdge(parent, null, "", vertexes[index], vertexes[adj])
                }
            }
        } finally {
            graph.model.endUpdate()
        }

        val graphComponent = mxGraphComponent(graph)
        contentPane.add(graphComponent)
    }
}