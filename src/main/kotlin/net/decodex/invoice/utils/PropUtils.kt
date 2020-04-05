package net.decodex.invoice.utils

import org.slf4j.LoggerFactory
import java.io.*
import java.util.*


object PropUtils {

    private const val PROPERTIES_FILE = "application.properties"
    private val LOG = LoggerFactory.getLogger(this::class.java)

    private val configFile = File(PROPERTIES_FILE)

    fun load(key: String): String? {
        return try {
            val reader = FileReader(configFile)
            val props = Properties()
            props.load(reader)
            val result: String = props.getProperty(key)
            reader.close()
            result
        } catch (ex: FileNotFoundException) {
            LOG.error("File not found", ex)
            null
        } catch (ex: IOException) {
            LOG.error("IC exception during the load", ex)
            null
        }
    }

    fun save(key: String, value: String) {
        try {
            val reader = FileReader(configFile)
            val props = Properties()
            props.load(reader)
            props.setProperty(key, value)
            val writer = FileWriter(configFile)
            props.store(writer, null)
            writer.close()
        } catch (ex: FileNotFoundException) {
            LOG.error("File not found", ex)
        } catch (ex: IOException) {
            LOG.error("IC exception during the save", ex)
        }
    }

}