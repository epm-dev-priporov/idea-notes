package dev.priporov.ideanotes.state

import java.io.File


class ApplicationStateDto {

    var appBaseDir = "${System.getProperty("user.home")}${fileSeparator}.ideanotesv2"

}