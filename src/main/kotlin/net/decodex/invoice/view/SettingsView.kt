package net.decodex.invoice.view

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.ImageLoader
import net.decodex.invoice.utils.LanguageUtils

class SettingsView private constructor() : Stage() {

    init {
        val root = FXLoader.load(VIEW_FILE)
        title = LanguageUtils.getString("settings")
        icons.add(ImageLoader.load(MainView.ICON_FILE))
        initModality(Modality.APPLICATION_MODAL)
        scene = Scene(root)
    }

    companion object {
        private const val VIEW_FILE = "view_settings.fxml"

        fun newInstance(): SettingsView {
            return SettingsView()
        }
    }
}