package net.decodex.invoice.view

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.FlowUtils
import net.decodex.invoice.utils.ImageLoader
import net.decodex.invoice.utils.LanguageUtils

class LoginView private constructor() : Stage() {

    init {
        val root = FXLoader.load(VIEW_FILE)
        title = LanguageUtils.getString("login")
        setOnCloseRequest { FlowUtils.closeApplication() }
        icons.add(ImageLoader.load(MainView.ICON_FILE))
        initModality(Modality.APPLICATION_MODAL)
        scene = Scene(root)
    }

    companion object {
        private const val VIEW_FILE = "view_login.fxml"

        fun newInstance(): LoginView {
            return LoginView()
        }
    }
}