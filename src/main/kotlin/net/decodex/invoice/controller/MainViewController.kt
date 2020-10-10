package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.ProgressBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import net.decodex.invoice.utils.*
import net.decodex.invoice.view.LoginView
import net.decodex.invoice.view.SettingsView
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*

class MainViewController : Initializable {

    @FXML
    private lateinit var statusLabel: Label

    @FXML
    private lateinit var spring: Region

    @FXML
    private lateinit var progressBar: ProgressBar

    @FXML
    private lateinit var contentView: BorderPane

    @FXML
    private lateinit var instructionsMenu: Menu

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

    @FXML
    fun openProductsView() {
        contentView.center = FXLoader.load("view_products.fxml")
    }

    @FXML
    fun openInvoicesView() {
        contentView.center = FXLoader.load("view_invoices.fxml")
    }

    @FXML
    fun openCourseView() {
        contentView.center = FXLoader.load("view_courses.fxml")
    }

    @FXML
    fun openDebtorsView() {
        TODO("Implement")
    }

    @FXML
    fun openUsersView() {
        TODO("Implement")
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        HBox.setHgrow(spring, Priority.ALWAYS)
        if (PropUtils.load("enableCourses") != "true") {
            LOG.info("Hiding the courses not enabled!")
            instructionsMenu.isVisible = false
        }
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

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}