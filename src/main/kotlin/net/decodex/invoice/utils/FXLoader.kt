package net.decodex.invoice.utils

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

object FXLoader {

    fun load(resource: String): Parent {
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("/$resource")
        loader.resources = LanguageUtils.languageResource
        return loader.load()
    }

    fun <T>load(resource: String): Pair<Parent, T> {
        val loader = FXMLLoader()
        loader.location = javaClass.getResource("/$resource")
        loader.resources = LanguageUtils.languageResource
        val root = loader.load<Parent>()
        val controller = loader.getController<T>()
        return Pair(root, controller)
    }
}