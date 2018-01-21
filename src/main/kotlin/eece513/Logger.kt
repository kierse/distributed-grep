package eece513

import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.writers.FileWriter
import org.pmw.tinylog.Logger as Tiny

interface Logger {
    fun debug(tag: String, msg: String, vararg args: Any)
    fun info(tag: String, msg: String, vararg args: Any)
    fun error(tag: String, msg: String, vararg args: Any)
}

class TinyLogWrapper(logLocation: String) : Logger {
    init {
        Configurator
                .currentConfig()
                .formatPattern("{date:yyyy-MM-dd HH:mm:ss} {thread} {level}: {message}")
                .level(Level.DEBUG)
                .writer(FileWriter(logLocation), Level.DEBUG)
                .activate()
    }

    override fun debug(tag: String, msg: String, vararg args: Any) = Tiny.debug(prepareMsg(tag, msg), *args)
    override fun info(tag: String, msg: String, vararg args: Any) = Tiny.info(prepareMsg(tag, msg), *args)
    override fun error(tag: String, msg: String, vararg args: Any) = Tiny.error(prepareMsg(tag, msg), *args)

    private fun prepareMsg(tag: String, msg: String) = "$tag:\t$msg"
}