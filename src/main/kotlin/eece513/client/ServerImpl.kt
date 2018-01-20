package eece513.client

import eece513.Logger
import java.net.InetAddress

class ServerImpl(
        private val ip: InetAddress,
        private val port: Int,
        override val name: String,
        private val logger: Logger
) : GrepClient.Server {

    override fun search(args: Array<String>, onResult: (GrepClient.Server.Response) -> Unit): GrepClient.Server.Query {
        val serverQuery = QueryImpl(
                ip = ip,
                port = port,
                id = name,
                args = args,
                logger = logger,
                onResponse = onResult
        )

        Thread(serverQuery).start()

        return serverQuery
    }
}