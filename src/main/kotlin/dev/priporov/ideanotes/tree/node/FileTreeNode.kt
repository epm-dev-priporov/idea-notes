package dev.priporov.ideanotes.tree.node


import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.state.NodeInfo
import dev.priporov.ideanotes.util.FileNodeUtils
import dev.priporov.ideanotes.util.FileNodeUtils.generateNodeName
import javax.swing.tree.DefaultMutableTreeNode

open class FileTreeNode(
    var name: String? = null,
    var extension: String? = null,
    var id: String? = generateNodeName(name),
    private var file: VirtualFile? = null,
    var type: NodeType?
) : DefaultMutableTreeNode() {
    constructor(node: FileTreeNode) : this(node.name, node.extension, node.id, type = node.type) {
        file = FileNodeUtils.initFile(id, node.extension)
    }

    constructor(node: NodeStateInfo) : this(node.name, node.extension, node.id, type = node.type) {
        file = FileNodeUtils.initFile(id, node.extension)
    }

    fun initFile() {
        file = FileNodeUtils.initFile(id, extension)
    }

    constructor(info: NodeCreationInfo) : this(
        name = info.name,
        extension = info.extension,
        type = info.type
    ) {
        id = generateNodeName(info.name)
        file = FileNodeUtils.initFile(id, info.extension)
    }

    constructor(nodeInfo: NodeInfo) : this(nodeInfo.name, nodeInfo.extension, nodeInfo.id, type = nodeInfo.type) {
        file = FileNodeUtils.initFile(id, nodeInfo.extension)
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

    fun generateNewId(): String {
        id = generateNodeName(name)
        val fileName = "${id}.${extension}"
        file?.rename(this, fileName)

        return id!!
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