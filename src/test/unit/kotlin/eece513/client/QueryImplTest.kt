package eece513.client

import eece513.DummyLogger
import org.junit.Test

import org.junit.Assert.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.math.exp

class QueryImplTest {
    private val ip = InetAddress.getByName("127.0.0.1")
    private val port = 6969

    @Test
    fun run__send_command_line_args() {
        val ready = AtomicBoolean(false)
        val args = arrayOf("command", "line", "args")

        thread {
            while (!ready.get()) { /* block until ready is true */ }

            QueryImpl(ip, port, "id", args, DummyLogger(), { }).run()
        }

        ServerSocket(port).use { ss ->
            ready.set(true)
            ss.accept().use { socket ->
                val isr = InputStreamReader(socket.inputStream)
                val br = BufferedReader(isr)

                assertArrayEquals(br.readLines().toTypedArray(), args)
            }
        }
    }

    @Test
    fun run__receive_single_line_result() {
        val expected = GrepClient.Server.Response.Result("id", listOf("foo bar baz"))

        thread {
            ServerSocket(port).use { ss ->
                ss.accept().use { socket ->
                    val osw = OutputStreamWriter(socket.outputStream)
                    val bw = BufferedWriter(osw)

                    bw.write("R:1\n")
                    bw.write(expected.result.first())
                    bw.flush()

                    socket.shutdownOutput()
                }
            }
        }

        QueryImpl(ip, port, "id", arrayOf(), DummyLogger()) { response ->
            assertEquals(response, expected)
        }.run()
    }

//    @Test
//    fun isComplete() {
//
//    }
}