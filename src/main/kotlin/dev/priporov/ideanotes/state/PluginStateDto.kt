package dev.priporov.ideanotes.state

import dev.priporov.ideanotes.tree.service.fileSeparator


class PluginStateDto {

    var appBaseDir = "${System.getProperty("user.home")}$fileSeparator.ideanotesv2"

}