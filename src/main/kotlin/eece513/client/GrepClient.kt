package eece513.client

import eece513.Logger
import eece513.TinyLogWrapper
import eece513.SERVER_IP
import eece513.SERVER_PORT
import java.util.concurrent.ConcurrentLinkedQueue

class GrepClient(
        private val presenter: Presenter,
        private val logger: Logger,
        private vararg val servers: Server
) {
    interface Presenter {
        fun displayResult(result: Server.Result)
    }

    interface Server {
        class Result(val name: String, val result: String)

        interface Query {
            fun isComplete(): Boolean
        }

        fun search(query: String, onResult: (Result) -> Unit): Query
    }

    companion object {
        private val tag: String = GrepClient::class.java.simpleName

        @JvmStatic
        fun main(args: Array<String>) {
            val presenter = PrintStreamPresenter(System.out)
            val logger = TinyLogWrapper()
            val servers = arrayOf(ServerImpl(SERVER_IP, SERVER_PORT, "server1"))

            GrepClient(presenter, logger, *servers)
                    .search(args)
        }
    }

    private fun search(args: Array<String>) {
        // Create queue to contain search results
        // Note: results from all servers will be available here
        val queue: ConcurrentLinkedQueue<Server.Result> = ConcurrentLinkedQueue()

        // Connection servers
        val query = args.joinToString(" ")
        logger.info(tag, "query: {}", query)

        var queries: List<Server.Query> = servers.map { server ->
            server.search(query) { result ->
                logger.debug(tag, "{} result: {}", result.name, result.result)
                queue.add(result)
            }
        }

        while (queries.isNotEmpty()) {
            while (true) {
                val result = queue.poll() ?: break
                presenter.displayResult(result)
            }

            queries = filterQueries(queries)
        }
    }

    private fun filterQueries(queries: List<Server.Query>): List<Server.Query> {
        return queries.filterNot { query ->
            query.isComplete()
        }
    }
}
