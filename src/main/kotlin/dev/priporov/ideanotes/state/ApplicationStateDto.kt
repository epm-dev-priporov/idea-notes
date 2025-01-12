package dev.priporov.ideanotes.state

import dev.priporov.ideanotes.tree.node.fileSeparator


class ApplicationStateDto {

    var appBaseDir = "${System.getProperty("user.home")}${fileSeparator}.ideanotesv2"

}