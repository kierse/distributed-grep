package eece513

import org.pmw.tinylog.Level
import java.net.InetAddress

val SERVER_IP: InetAddress = InetAddress.getByName("127.0.0.1")
const val SERVER_PORT = 6969

const val GREP_CMD = "/usr/bin/grep"

const val SERVER_LOG_LOCATION = "logs/grep-server.log"
const val CLIENT_LOG_LOCATION = "logs/grep-client.log"
const val GREP_DATA_LOCATION = "logs/machine.log"
