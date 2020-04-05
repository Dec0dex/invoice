package net.decodex.invoice.view

import javafx.scene.Scene
import javafx.stage.Stage
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.ImageLoader
import net.decodex.invoice.utils.LanguageUtils

class MainView private constructor() : Stage() {

    init {
        val root = FXLoader.load(VIEW_FILE)
        title = LanguageUtils.getString("app_name")
        isMaximized = true
        icons.add(ImageLoader.load(ICON_FILE))
        scene = Scene(root)
    }

    companion object {
        private const val VIEW_FILE = "view_main.fxml"
        const val ICON_FILE = "icon.png"

        fun newInstance(): MainView {
            return MainView()
        }
    }
}