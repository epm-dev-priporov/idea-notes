package dev.priporov.ideanotes.tree.common

class ExtensionFileHelper {
    companion object {
        val EXTENSIONS = listOf<ExtensionData>(
            ExtensionData("txt", "Text node"),
            ExtensionData("json", "Json node"),
            ExtensionData("xml", "Xml node"),
            ExtensionData("yaml", "Yaml node"),
            ExtensionData("sql", "Sql node"),
            ExtensionData("http", "Http node")
        )
    }

}

class ExtensionData(val extension: String, val definition: String)
