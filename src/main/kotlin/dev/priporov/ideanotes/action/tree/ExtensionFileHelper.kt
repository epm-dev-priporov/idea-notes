package dev.priporov.ideanotes.action.tree

class ExtensionFileHelper {
    companion object {
        val EXTENSIONS = listOf<ExtensionData>(
            ExtensionData("txt", "Text node"),
            ExtensionData("json", "Json node"),
            ExtensionData("xml", "Xml node"),
            ExtensionData("yaml", "Yaml node"),
            ExtensionData("sql", "Sql node")
        )
    }

}

class ExtensionData(val extension: String, val definition: String)
