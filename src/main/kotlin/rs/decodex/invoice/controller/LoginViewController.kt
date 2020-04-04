package rs.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import rs.decodex.invoice.utils.FlowUtils

class LoginViewController {

    @FXML
    private lateinit var gridView: GridPane

    @FXML
    fun cancel() {
        FlowUtils.closeApplication()
    }

    @FXML
    fun login() {
        (gridView.scene.window as Stage).close()
    }
}