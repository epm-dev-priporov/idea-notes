package dev.priporov.ideanotes.tree.service

import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.node.NoteNode

abstract class BaseTreeStateService {

    lateinit var treeState: TreeStateDto

    open fun updateNodeOrder(node: NoteNode) {
        val children: MutableList<String> = node.children()
            .asSequence()
            .mapNotNull { it as NoteNode }
            .map { it.id }
            .filterNotNull()
            .distinct()
            .toMutableList()

        if(children.isEmpty()){
            treeState.hierarchy.remove(node.id)
        } else {
            treeState.hierarchy[node.id!!] = children
        }
        saveStateFile(treeState)
    }

    protected abstract fun saveStateFile(treeState: TreeStateDto)

}