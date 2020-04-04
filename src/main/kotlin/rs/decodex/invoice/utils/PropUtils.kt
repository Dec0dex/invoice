package rs.decodex.invoice.utils

import java.io.*
import java.util.*


object PropUtils {

    private const val PROPERTIES_FILE = "application.properties"

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
            null
        } catch (ex: IOException) {
            null
        }
    }

    fun save(key: String, value: String) {
        try {
            val props = Properties()
            props.setProperty(key, value)
            val writer = FileWriter(configFile)
            props.store(writer, null)
            writer.close()
        } catch (ex: FileNotFoundException) {
            // file does not exist
        } catch (ex: IOException) {
            // I/O error
        }
    }

}