package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import net.decodex.invoice.api.Api
import net.decodex.invoice.model.dto.AuthRequestDTO
import net.decodex.invoice.utils.FlowUtils
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.launchOnFxThread
import org.slf4j.LoggerFactory


class LoginViewController {

    @FXML
    private lateinit var gridView: GridPane

    @FXML
    private lateinit var username: TextField

    @FXML
    private lateinit var password: PasswordField

    @FXML
    fun cancel() {
        FlowUtils.closeApplication()
    }

    @FXML
    fun login() {
        val request = AuthRequestDTO(username.text, password.text)
        GlobalScope.launch() {
            try {
                val response = Api.authApi.authorize(request).await()
                Api.TOKEN = response.token
                LOG.debug("API Token: ${Api.TOKEN}")
                launchOnFxThread { (gridView.scene.window as Stage).close() }
            } catch (ex: Exception) {
                showLoginFailedDialog()
                LOG.error("Login attempt failed", ex)
            }
        }
    }

    private fun showLoginFailedDialog() {
        launchOnFxThread {
            val alert = Alert(AlertType.INFORMATION)
            alert.title = LanguageUtils.getString("login")
            alert.headerText = null
            alert.contentText = LanguageUtils.getString("login_failed")
            alert.showAndWait()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LoginViewController::class.java)
    }
}