package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.FlowUtils
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.launchOnFxThread
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
    private lateinit var contentView: BorderPane

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

    @FXML
    fun openClientsView() {
        contentView.center = FXLoader.load("view_clients.fxml")
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        HBox.setHgrow(spring, Priority.ALWAYS)
    }

    fun setProgress(progress: Double) {
        launchOnFxThread {
            progressBar.isDisable = false
            progressBar.progress = progress
        }
    }

    fun resetStatus() {
        launchOnFxThread {
            progressBar.isDisable = true
            progressBar.progress = 0.0
            statusLabel.text = LanguageUtils.getString("idle")
        }
    }

    fun setStatusText(status: String) {
        launchOnFxThread {
            statusLabel.text = status
        }
    }
}