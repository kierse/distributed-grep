package eece513.client

import java.io.*
import java.net.InetAddress

class FileIO{
    fun ReadLinesAsInetAddress(path:String): ArrayList<InetAddress> {
        val inputStream: InputStream = File(path).inputStream()
        val lineList = ArrayList<InetAddress>()

        inputStream.bufferedReader().useLines { lines -> lines.forEach {
            lineList.add(InetAddress.getByName(it))
        }}
        return lineList
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