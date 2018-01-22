package eece513.client

import java.io.*
import java.net.InetAddress

class FileIO{
    fun ReadLinesAsInetAddress(path:String): List<InetAddress> {
        val inputStream: InputStream = File(path).inputStream()

        return inputStream
                .bufferedReader()
                .readLines()
                .filter { it.isNotEmpty() }
                .map {
                    InetAddress.getByName(it)
                }
    }

    fun GetGrepDataLoc():String{
        val regex = Regex("""machine\.\d\.log$""")
        File(System.getProperty("user.dir")).walkTopDown().forEach { file ->
            if (file.name.matches(regex)) {
                return file.absolutePath
            }
        }

        return "machine.*.log file not found"
    }
}