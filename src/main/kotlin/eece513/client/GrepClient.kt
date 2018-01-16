package eece513.client

import eece513.SERVER_IP
import eece513.SERVER_PORT
import java.io.BufferedReader
import java.io.Closeable

class GrepClient(private val server: Server) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = GrepClientServerImpl(SERVER_IP, SERVER_PORT)
            GrepClient(server).query(args)
        }
    }

    interface Server {
        interface Connection  : Closeable {
            fun writeArguments(args: String)
            fun readLogData(): BufferedReader
        }

        fun connect(): Connection
    }

    private fun query(args: Array<String>) {
        println("connecting to $SERVER_IP:$SERVER_PORT...")

        server.connect().use { connection ->
            println("connected to $SERVER_IP:$SERVER_PORT")

            // join command line args with space and write to connection
            connection.writeArguments(args.joinToString(" "))

            println("reading data from socket...")

            // read response from connection line by line
            val reader = connection.readLogData()
            do {
                val line = reader.readLine()
                if (line != null) println(line)
            } while (line != null)
        }
    }
}