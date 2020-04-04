package rs.decodex.invoice.controller

import javafx.fxml.FXML
import rs.decodex.invoice.utils.FlowUtils
import rs.decodex.invoice.view.LoginView
import rs.decodex.invoice.view.SettingsView

class MainViewController {

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
}