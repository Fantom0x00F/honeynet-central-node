package com.fantom0x00f.entity

class NodeGroup(val id: Int, val name: String) {
    val adjacents: MutableList<Int> = mutableListOf()
    val availableAgents: MutableList<Agent> = mutableListOf()
}