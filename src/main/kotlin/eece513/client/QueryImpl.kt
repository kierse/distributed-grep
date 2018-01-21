package eece513.client

import eece513.Logger
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
        private val args: Array<String>,
        private val logger: Logger,
        private val onResult: (GrepClient.Server.Result) -> Unit
) : GrepClient.Server.Query, Runnable {
    private val tag = QueryImpl::class.java.simpleName
    private val moreResults = AtomicBoolean(true)

    override fun run() {
        Socket(ip, port).use { socket ->
            val osw = OutputStreamWriter(socket.getOutputStream())
            val bw = BufferedWriter(osw)

            // send query args, one-by-one
            logger.debug(tag, "sending ${args.size} args...")

            for (arg in args) {
                logger.debug(tag, "arg: $arg")
                bw.write("$arg\n")
            }
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