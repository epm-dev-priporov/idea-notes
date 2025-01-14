package dev.priporov.ideanotes.state

abstract class BaseTreeState {

    protected abstract fun saveStateFile(treeState: TreeStateDto)

}