package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.state.BaseTreeState
import dev.priporov.ideanotes.state.TreeStateDto

@Service
class ProjectTreeStateService: BaseTreeState() {

    override fun saveStateFile(treeState: TreeStateDto) {

    }
    
}