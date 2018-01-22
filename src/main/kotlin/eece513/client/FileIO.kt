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
        File(System.getProperty("user.dir")).walkTopDown().forEach {
            val fileName = it.name.split("\\").last()
            if(fileName.startsWith("machine.") && fileName.endsWith(".log")){
                return fileName
            }
        }

        return "machine.*.log file not found"
    }
}