package eece513.client

import java.io.*
import java.net.InetAddress

class FileIO{
    fun readLinesAsInetAddress(path:String): ArrayList<InetAddress> {
        val inputStream: InputStream = File(path).inputStream()
        val lineList = ArrayList<InetAddress>()

        inputStream.bufferedReader().useLines { lines -> lines.forEach {
            lineList.add(InetAddress.getByName(it))
        }}
        return lineList
    }
}