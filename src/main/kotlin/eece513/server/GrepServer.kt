package eece513.server

import eece513.*

class GrepServer(
        private val queryService: QueryService,
        private val connectionListener: ConnectionListener,
        private val logger: Logger
) {
    interface QueryService {
        fun search(query: String, onResult: (String) -> Unit)
    }

    interface ConnectionListener {
        interface Connection {
            fun getQuery(): String
            fun sendResult(result: String)
        }

        fun listen(onQuery: (Connection) -> Unit)
    }

    companion object {
        private val tag: String = GrepServer::class.java.simpleName

        @JvmStatic
        fun main(args: Array<String>) {
            val logger = TinyLogWrapper()
            val queryService = GrepQueryService(GREP_CMD, LOG_LOCATION, logger)
            val queryListener = SocketConnectionListener(SERVER_PORT, logger)

            GrepServer(queryService, queryListener, logger)
                    .run()
        }
    }

    private fun run() {
        logger.info(tag, "booting up...")

        connectionListener.listen { connection ->
            val query = connection.getQuery()
            logger.debug(tag, "received query: {}", query)

            queryService.search(query) { result ->
                logger.debug(tag, "found result: {}", result)
                connection.sendResult("$result\n")
            }

            logger.debug(tag, "no more results!")
        }
    }
}