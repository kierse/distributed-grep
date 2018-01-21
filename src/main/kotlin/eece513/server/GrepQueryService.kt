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

    override fun search(
            args: Array<String>, onResult: (String) -> Unit, onError: (Array<String>) -> Unit
    ) {
        val argsList = arrayOf(cmd, *args, logFile)
        logger.debug(tag, "grep cmd: {}", argsList.joinToString(" "))

        var esr: InputStreamReader? = null
        var isr: InputStreamReader? = null

        var ebr: BufferedReader? = null
        var ibr: BufferedReader? = null

        try {
            val proc = ProcessBuilder(*argsList).start()

            esr = InputStreamReader(proc.errorStream)
            ebr = BufferedReader(esr)

            isr = InputStreamReader(proc.inputStream)
            ibr = BufferedReader(isr)

            if (ebr.ready()) {
                onError.invoke(ebr.readLines().toTypedArray())
                return
            }

            while (true) {
                val line = ibr.readLine() ?: break
                onResult.invoke(line)
            }

            logger.debug(tag, "no more results!")
        } finally {
            esr?.close()
            isr?.close()

            ebr?.close()
            ibr?.close()
        }
    }
}