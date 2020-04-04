package rs.decodex.invoice.utils

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

object FXLoader {

    fun load(resource: String): Parent {
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("/$resource")
        loader.resources = LanguageUtils.languageResource
        return loader.load()
    }
}