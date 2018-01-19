package eece513.client

import java.net.InetAddress

class ServerImpl(private val ip: InetAddress, private val port: Int, private val name: String) : GrepClient.Server {

    override fun search(query: String, onResult: (GrepClient.Server.Result) -> Unit): GrepClient.Server.Query {
        val serverQuery = QueryImpl(
                ip = ip,
                port = port,
                id = name,
                query = query,
                onResult = onResult
        )

        Thread(serverQuery).start()

        return serverQuery
    }
}