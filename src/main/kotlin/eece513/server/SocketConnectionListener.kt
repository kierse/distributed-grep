package eece513.server

import eece513.Logger
import java.net.ServerSocket

class SocketConnectionListener(private val port: Int, private val logger: Logger) : GrepServer.ConnectionListener {
    private val tag = SocketConnectionListener::class.java.simpleName

    override fun listen(onQuery: (GrepServer.ConnectionListener.Connection) -> Unit) {
        ServerSocket(port).use { serverSocket -> // bind to port
            logger.debug(tag, "binding to port: $port")

            while (true) {
                serverSocket.accept().use { socket -> // listen for incoming connection
                    logger.info(tag, "new connection on port: $port")

                    onQuery(ConnectionImpl(socket, logger))

                    // inform client that we're done sending data..
                    logger.debug(tag, "closing output stream")
                    socket.shutdownOutput()

                    /* wait until client has read all data and closed connection */
//                logger.debug(tag, "waiting for client to close connection...")
//                while (!socket.isClosed) { }

//                logger.debug(tag, "client closed connection. Terminating socket!")
                }
            }
        }
    }
}