package eece513.server

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class ConnectionImpl(socket: Socket) : GrepServer.ConnectionListener.Connection {
    private val br: BufferedReader
    private val bw: BufferedWriter

    init {
        val isr = InputStreamReader(socket.getInputStream())
        br = BufferedReader(isr)

        val osw = OutputStreamWriter(socket.getOutputStream())
        bw = BufferedWriter(osw)
    }

    override fun getQuery(): String = br.readLine() ?: ""

    override fun sendResult(result: String) {
        bw.write(result)
        bw.flush()
    }
}