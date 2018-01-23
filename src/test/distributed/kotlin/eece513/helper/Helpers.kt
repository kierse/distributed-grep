package eece513.helper

import java.io.File

fun pathToAssets(): String {
    return arrayOf(System.getProperty("user.dir"), "src", "main", "kotlin", "eece513", "assets", "servers.txt")
            .joinToString(File.separator)
}