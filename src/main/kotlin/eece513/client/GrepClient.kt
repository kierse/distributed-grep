package eece513.client

import eece513.*
import java.util.concurrent.ConcurrentLinkedQueue

class GrepClient(
        private val presenter: Presenter,
        private val helpGenerator: HelpGenerator,
        private val logger: Logger,
        private vararg val servers: Server
) {
    interface Presenter {
        fun displayResponse(response: Server.Response)
        fun displayHelp(msg: String)
    }

    interface Server {
        sealed class Response {
            data class Result(val name: String, val result: List<String>) : Response()
            data class Error(val name: String, val result: List<String>) : Response()
        }

        interface Query {
            fun isComplete(): Boolean
        }

        val name: String

        fun search(args: Array<String>, onResult: (Response) -> Unit): Query
    }

    interface HelpGenerator {
        fun getHelpMessage(): String
    }

    companion object {
        private val tag: String = GrepClient::class.java.simpleName

        @JvmStatic
        fun main(args: Array<String>) {
            val presenter = PrintStreamPresenter(System.out, System.err)
            val logger = TinyLogWrapper(CLIENT_LOG_LOCATION)
            val helpGenerator = GrepHelpGenerator(GREP_CMD, logger)

            FileIO().ReadLinesAsInetAddress(System.getProperty("user.dir") + "/servers.txt").forEach {
                val servers = arrayOf(ServerImpl(it, SERVER_PORT, it.hostAddress, logger))
//                val servers = arrayOf(ServerImpl(SERVER_IP, SERVER_PORT, "ec2-35-183-26-44.ca-central-1.compute.amazonaws.com", logger))


                GrepClient(presenter, helpGenerator, logger, *servers)
                        .search(args)
            }
        }
    }

    internal fun search(args: Array<String>) {
        if (args.isEmpty()) {
            presenter.displayHelp(helpGenerator.getHelpMessage())
            return
        }

        // Create queue to contain search results
        // Note: results from all servers will be available here
        val queue: ConcurrentLinkedQueue<Server.Response> = ConcurrentLinkedQueue()

        // Connection servers
        logger.info(tag, "args: [{}]", args.joinToString(", "))
        var queries: List<Server.Query> = servers.map { server ->
            logger.debug(tag, "searching ${server.name}...")
            server.search(args) { response ->
                logger.debug(tag, "response: {}", response)
                queue.add(response)
            }
        }

        while (queries.isNotEmpty()) {
            while (true) {
                val result = queue.poll() ?: break
                presenter.displayResponse(result)
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
