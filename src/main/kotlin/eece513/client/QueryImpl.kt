package eece513.client

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

class QueryImpl(
        private val ip: InetAddress,
        private val port: Int,
        private val id: String,
        private val query: String,
        private val onResult: (GrepClient.Server.Result) -> Unit
) : GrepClient.Server.Query, Runnable {

    private val moreResults = AtomicBoolean(true)

    override fun run() {
        Socket(ip, port).use { socket ->
            val osw = OutputStreamWriter(socket.getOutputStream())
            val bw = BufferedWriter(osw)

            // send query
            bw.write(query)
            bw.flush()

            // indicate no more data
            socket.shutdownOutput()

            val isr = InputStreamReader(socket.getInputStream())
            val br = BufferedReader(isr)

            while (true) {
                val line = br.readLine() ?: break
                val result = GrepClient.Server.Result(id, line)
                onResult.invoke(result)
            }

            socket.shutdownInput()
            moreResults.set(false)
        }
    }

    override fun isComplete(): Boolean = !moreResults.get()
}