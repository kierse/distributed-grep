package eece513.server

import eece513.*
import eece513.client.FileIO

class GrepServer(
        private val queryService: QueryService,
        private val connectionListener: ConnectionListener,
        private val logger: Logger
) {
    interface QueryService {
        fun search(args: Array<String>, onResult: (String) -> Unit, onError: (Array<String>) -> Unit)
    }

    interface ConnectionListener {
        interface Connection {
            fun getQueryArgs(): Array<String>
            fun sendResult(result: String)
            fun sendError(error: Array<String>)
        }

        fun listen(onQuery: (Connection) -> Unit)
    }

    companion object {
        private val tag: String = GrepServer::class.java.simpleName

        @JvmStatic
        fun main(args: Array<String>) {
            val logger = TinyLogWrapper(SERVER_LOG_LOCATION)
            val grepDataLoc = FileIO().GetGrepDataLoc()
            val queryService = GrepQueryService(GREP_CMD, grepDataLoc, logger)
            val queryListener = SocketConnectionListener(SERVER_PORT, logger)

            GrepServer(queryService, queryListener, logger)
                    .run()
        }
    }

    internal fun run() {
        logger.info(tag, "booting up...")

        connectionListener.listen { connection ->
            val args = connection.getQueryArgs()
            logger.debug(tag, "query args: [{}]", args.joinToString(", "))

            queryService.search(
                    args,
                    { connection.sendResult(it) },
                    { connection.sendError(it) }
            )
        }
    }
}