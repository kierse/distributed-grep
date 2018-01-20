package eece513.client

import java.io.PrintStream

class PrintStreamPresenter(
        private val out: PrintStream, private val err: PrintStream
) : GrepClient.Presenter {
    override fun displayResponse(response: GrepClient.Server.Response) {
        when (response) {
            is GrepClient.Server.Response.Result -> printStdOut(response)
            is GrepClient.Server.Response.Error -> printStdErr(response)
        }
    }

    private fun printStdOut(response: GrepClient.Server.Response.Result) {
        for (line in response.result) {
            out.println("${response.name}:$line")
        }
    }

    private fun printStdErr(response: GrepClient.Server.Response.Error) {
        for (line in response.result) {
            err.println("${response.name}:$line")
        }
    }
}