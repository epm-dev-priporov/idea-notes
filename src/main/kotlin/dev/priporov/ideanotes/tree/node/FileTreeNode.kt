package dev.priporov.ideanotes.tree.node


import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.state.NodeInfo
import dev.priporov.ideanotes.util.FileNodeUtils.Companion.generateNodeName
import dev.priporov.ideanotes.util.FileNodeUtils.Companion.initFile
import javax.swing.tree.DefaultMutableTreeNode

open class FileTreeNode(
    var name: String? = null,
    var extension: String? = null,
    var id: String? = generateNodeName(name),
    private var file: VirtualFile? = null,
) : DefaultMutableTreeNode() {
    constructor(node: FileTreeNode) : this(node.name, node.extension, node.id) {
        file = initFile(id, node.extension)
    }

    constructor(info: NodeCreationInfo) : this(
        name = info.name,
        extension = info.extension
    ) {
        id = generateNodeName(info.name)
        file = initFile(id, info.extension)
    }

    constructor(nodeInfo: NodeInfo) : this(nodeInfo.name, nodeInfo.extension, nodeInfo.id){
        file = initFile(id, nodeInfo.extension)
    }

    init {
        userObject = name
    }

    fun rename(newName: String) {
        id = "${newName}_${id?.substringAfterLast("_")}"
        val fileName = "${id}.${extension}"
        file?.rename(this, fileName)
        name = newName
        userObject = newName
    }

    fun delete() {
        children?.filterNotNull()?.forEach { (it as FileTreeNode).delete() }
        if (file?.exists() == true) {
            file?.delete(this)
        }
        removeFromParent()
    }

    fun getFile() = file

}