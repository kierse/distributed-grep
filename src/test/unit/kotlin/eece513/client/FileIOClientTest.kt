package eece513.client

import org.junit.Test
import org.junit.Assert.*

class FileIOClientTest{
    private val path = System.getProperty("user.dir") + "\\src\\test\\unit\\kotlin\\eece513\\DummyServer.txt"

    @Test
    fun print(){
        println(path)
    }

    @Test
    fun read__lines_from_file(){
        val servers = FileIO().ReadLinesAsInetAddress(path)

        assertEquals(servers.first().hostAddress, "127.0.0.1")
        assertTrue(servers.first().hostName.contains("127"))
    }
}