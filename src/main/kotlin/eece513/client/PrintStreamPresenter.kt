package eece513.client

import java.io.PrintStream

class PrintStreamPresenter(private val stream: PrintStream) : GrepClient.Presenter {
    override fun displayResult(result: GrepClient.Server.Result) {
        stream.println("${result.name}:${result.result}")
    }
}