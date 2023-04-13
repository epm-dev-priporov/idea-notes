package dev.priporov.ideanotes.extension

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.SearchScopeProvider
import dev.priporov.ideanotes.tree.common.VirtualFileContainer

private const val SCOPE_NAME = "NotesTree"

class NoteSearchScopeProvider : SearchScopeProvider {

    private val virtualFileContainer = service<VirtualFileContainer>()

    override fun getDisplayName() = SCOPE_NAME

    override fun getSearchScopes(project: Project, dataContext: DataContext): MutableList<SearchScope> {
        val scope = GlobalSearchScope.filesScope(project, virtualFileContainer.getFiles(), SCOPE_NAME)
        return mutableListOf(scope)
    }

}
