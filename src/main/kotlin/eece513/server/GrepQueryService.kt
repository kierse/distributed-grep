package eece513.server

import eece513.Logger
import java.io.BufferedReader
import java.io.InputStreamReader

class GrepQueryService(
        private val cmd: String,
        private val logFile: String,
        private val logger: Logger
) : GrepServer.QueryService {
    private val tag = GrepQueryService::class.java.simpleName

    override fun search(query: String, onResult: (String) -> Unit) {
        logger.debug(tag, "grep cmd: {} {} {}", cmd, query, logFile)

        val runtime = Runtime.getRuntime()

        val args = mutableListOf(cmd)
        args.addAll(query.split(" "))
        args.add(logFile)

        val proc = runtime.exec(args.toTypedArray())

        val isr = InputStreamReader(proc.inputStream)
        val br = BufferedReader(isr)

        while (true) {
            val line = br.readLine() ?: break
            onResult.invoke(line)
        }

        br.close()
    }
}