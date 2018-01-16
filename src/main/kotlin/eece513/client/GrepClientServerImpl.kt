package eece513.client

import java.io.*
import java.net.InetAddress
import java.net.Socket

class GrepClientServerImpl(private val ip: InetAddress, private val port: Int) :
        GrepClient.Server, GrepClient.Server.Connection {
    private lateinit var socket: Socket

    override fun connect(): GrepClient.Server.Connection {
        println("opening socket to $ip:$port")
        socket = Socket(ip, port)
        return this
    }

    override fun writeArguments(args: String) {
        val osw = OutputStreamWriter(socket.getOutputStream())

        val writer = BufferedWriter(osw)
        println("writing ${args.length} chars to socket")
        writer.write(args)
        writer.flush()
        socket.shutdownOutput()
    }

    override fun readLogData(): BufferedReader {
        val isr  = InputStreamReader(socket.getInputStream())
        return BufferedReader(isr)
    }

    override fun close() = socket.close()
}