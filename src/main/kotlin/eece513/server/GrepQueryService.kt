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

    override fun search(args: Array<String>, onResult: (String) -> Unit) {
        val argsList = arrayOf(cmd, *args, logFile)
        logger.debug(tag, "grep cmd: {}", argsList.joinToString(" "))

        val proc = ProcessBuilder(*argsList).start()

        val isr = InputStreamReader(proc.inputStream)
        val br = BufferedReader(isr)

        while (true) {
            val line = br.readLine() ?: break
            onResult.invoke(line)
        }

        br.close()
    }
}