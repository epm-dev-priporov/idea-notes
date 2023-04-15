package dev.priporov.ideanotes.extension

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.SearchScopeProvider
import dev.priporov.ideanotes.tree.common.VirtualFileContainer


class NoteSearchScopeProvider : SearchScopeProvider {

    private val virtualFileContainer = service<VirtualFileContainer>()

    override fun getDisplayName() = "NotesTree"

    override fun getSearchScopes(project: Project, dataContext: DataContext): MutableList<SearchScope> {
        val scope = GlobalSearchScope.filesScope(project, virtualFileContainer.getFiles(), "notes")
        if (scope == GlobalSearchScope.EMPTY_SCOPE) {
            return mutableListOf(EmptyScope("Notes"))
        }
        return mutableListOf(scope)
    }

}

class EmptyScope(val name: String) : GlobalSearchScope() {
    override fun contains(file: VirtualFile): Boolean = false
    override fun isSearchInModuleContent(aModule: Module): Boolean = false
    override fun isSearchInLibraries(): Boolean = false

    override fun getDisplayName(): String = name
}