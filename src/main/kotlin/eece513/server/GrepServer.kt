package eece513.server

import eece513.SERVER_PORT
import java.io.*
import java.net.ServerSocket

class GrepServer(private val port: Int) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            GrepServer(SERVER_PORT).listen()
        }
    }

    private val data = """
Lorem Ipsum is simply dummy text of the printing and typesetting
industry. Lorem Ipsum has been the industry's standard dummy text
ever since the 1500s, when an unknown printer took a galley of type
and scrambled it to make a type specimen book. It has survived not
only five centuries, but also the leap into electronic typesetting,
remaining essentially unchanged. It was popularised in the 1960s
with the release of Letraset sheets containing Lorem Ipsum passages,
and more recently with desktop publishing software like Aldus
PageMaker including versions of Lorem Ipsum.
        """

    private fun listen() {
        println("binding to port $port")

        ServerSocket(port).use { serverSocket ->  // close serverSocket
            println("bound to port $port")

            // set timeout so that server eventually gives up and terminates
            // 10 seconds in milliseconds
            serverSocket.soTimeout = 10 * 1000

            println("listening for connection on port $port")

            serverSocket.accept().use { socket -> // close socket
                println("new connection on port $port")

                val isr = InputStreamReader(socket.getInputStream())
                val args = BufferedReader(isr).readLine() ?: ""

                if (args.isNotEmpty()) println("args: $args")

                val osw = OutputStreamWriter(socket.getOutputStream())

                val writer = BufferedWriter(osw)
                println("sending ${data.length} chars to client")
                writer.write(data)
                writer.flush()
                socket.shutdownOutput()
            }
        }
    }
}