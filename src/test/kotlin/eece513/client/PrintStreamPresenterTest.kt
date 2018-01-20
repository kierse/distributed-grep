package eece513.client

import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PrintStreamPresenterTest {
    @Test
    fun displayResult() {
        val result = GrepClient.Server.Response("name", "result")
        val arrayStream = ByteArrayOutputStream()

        PrintStreamPresenter(PrintStream(arrayStream))
                .displayResponse(result)

        // Note: trailing \n is because presenter calls println()
        assertEquals(arrayStream.toString(), "${result.name}:${result.result}\n")
    }
}