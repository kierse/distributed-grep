package eece513.server

import eece513.Logger
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean

class SocketConnectionListener internal constructor(
        private val port: Int, private val logger: Logger, private val loop: AtomicBoolean
) : GrepServer.ConnectionListener {
    private val tag = SocketConnectionListener::class.java.simpleName

    constructor(port: Int, logger: Logger): this(port, logger, AtomicBoolean(true))

    override fun listen(onQuery: (GrepServer.ConnectionListener.Connection) -> Unit) {
        ServerSocket(port).use { serverSocket -> // bind to port
            logger.debug(tag, "binding to port: $port")

            while (loop.get()) {
                serverSocket.accept().use { socket -> // listen for incoming connection
                    logger.info(tag, "new connection on port: $port")

                    onQuery(ConnectionImpl(socket, logger))

                    // inform client that we're done sending data..
                    logger.debug(tag, "closing output stream")
                    socket.shutdownOutput()
                }
            }
        }
    }
}