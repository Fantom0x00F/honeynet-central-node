package com.fantom0x00f.genetic.test_visualization

import com.fantom0x00f.entity.NodeGroup
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import javax.swing.JFrame

class Visual(nodes: List<NodeGroup>) : JFrame("Hello") {

    init {
        val graph = mxGraph()
        val parent = graph.defaultParent

        graph.model.beginUpdate()
        try {
            val vertexes = nodes.map { graph.insertVertex(parent, null, "", Math.random() * 500, Math.random() * 500, 10.0, 10.0) }.toList()

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