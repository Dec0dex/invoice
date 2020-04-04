package rs.decodex.invoice.utils

import java.util.*


object LanguageUtils {

    init {
        loadLocaleFromProperties()
    }

    var languageResource = ResourceBundle.getBundle("strings")

    fun loadLocaleFromProperties() {
        val valueFromFile = PropUtils.load("language")?.split("_")
        Locale.setDefault(Locale(valueFromFile?.first(), valueFromFile?.last()))
        languageResource = ResourceBundle.getBundle("strings")
    }

    fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        PropUtils.save("language", locale.toString())
        languageResource = ResourceBundle.getBundle("strings")
    }

    fun getString(key: String): String {
        return languageResource.getString(key)
    }
}