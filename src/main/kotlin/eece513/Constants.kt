package eece513

import org.pmw.tinylog.Level
import java.net.InetAddress

val SERVER_IP: InetAddress = InetAddress.getByName("ec2-35-183-26-44.ca-central-1.compute.amazonaws.com")
const val SERVER_PORT = 6969

const val GREP_CMD = "grep"

const val SERVER_LOG_LOCATION = "logs/grep-server.log"
const val CLIENT_LOG_LOCATION = "logs/grep-client.log"
const val GREP_DATA_LOCATION = "machine.1.log"

