package eece513.client

import eece513.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Instances of this class can be used to perform searches of log files on a list of provided remote servers.
 */
class GrepClient(
        private val presenter: Presenter,
        private val helpGenerator: HelpGenerator,
        private val logger: Logger,
        private val servers: List<Server>
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

            val servers = FileIO().ReadLinesAsInetAddress(System.getProperty("user.dir") + "/servers.txt").map { address ->
                ServerImpl(address, SERVER_PORT, address.hostAddress, logger)
            }

            GrepClient(presenter, helpGenerator, logger, servers)
                    .search(args)
        }
    }

    init {
        if (servers.isEmpty()) throw IllegalArgumentException("must provide at least one server!")
    }

    /**
     * Calling this method with a list of query args will trigger searches on each configured remote server. Each
     * search will be run on its own thread with all results being piped to the [presenter] on the main thread.
     */
    fun search(args: Array<String>) {
        if (args.isEmpty()) {
            presenter.displayHelp(helpGenerator.getHelpMessage())
            return
        }

        // Create queue to contain search results
        // Note: results from all servers will be available here so this Collection must be thread safe!
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

        // loop until our list of Query objects is empty
        while (queries.isNotEmpty()) {

            // loop and grab results from the top of the queue until there is nothing left
            while (true) {
                val result = queue.poll() ?: break
                presenter.displayResponse(result)
            }

            // filter the list of Query objects for ones that are still producing results..
            queries = filterQueries(queries)
        }
    }

    private fun filterQueries(queries: List<Server.Query>): List<Server.Query> {
        return queries.filterNot { query ->
            query.isComplete()
        }
    }
}
