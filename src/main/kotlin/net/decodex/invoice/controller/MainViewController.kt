package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import net.decodex.invoice.utils.FlowUtils
import net.decodex.invoice.view.LoginView
import net.decodex.invoice.view.SettingsView
import java.net.URL
import java.util.*

class MainViewController: Initializable {

    @FXML
    private lateinit var statusLabel: Label

    @FXML
    private lateinit var spring: Region

    @FXML
    private lateinit var progressBar: ProgressBar

    @FXML
    fun lockProgram() {
        LoginView.newInstance().showAndWait()
    }

    @FXML
    fun exitApplication() {
        FlowUtils.closeApplication()
    }

    @FXML
    fun openSettings() {
        SettingsView.newInstance().showAndWait()
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        HBox.setHgrow(spring, Priority.ALWAYS)
    }
}