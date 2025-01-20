package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.state.TreeStateDto

@Service
class ProjectTreeStateService : BaseTreeStateService() {

    override fun saveStateFile(treeState: TreeStateDto) {

    }

}