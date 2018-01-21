package eece513.client

import eece513.Logger
import java.io.BufferedReader
import java.io.InputStreamReader

class GrepHelpGenerator(private val cmd: String, private val logger: Logger) : GrepClient.HelpGenerator {
    private val tag = GrepHelpGenerator::class.java.simpleName

    override fun getHelpMessage(): String {
        var esr: InputStreamReader? = null
        var ebr: BufferedReader? = null

        val help = mutableListOf<String>()
        try {
            val proc = ProcessBuilder(cmd, "--help").start()

            esr = InputStreamReader(proc.errorStream)
            ebr = BufferedReader(esr)

            while (true) {
                val line = ebr.readLine() ?: break
                help.add(line)
            }
        } finally {
            esr?.close()
            ebr?.close()
        }

        return help.joinToString("\n")
    }
}