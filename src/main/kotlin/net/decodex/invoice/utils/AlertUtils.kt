package net.decodex.invoice.utils

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType


object AlertUtils {

    fun showFailedToLoadData() {
        launchOnFxThread {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = LanguageUtils.getString("network_error")
            alert.headerText = null
            alert.contentText = LanguageUtils.getString("failed_to_load_data")
            alert.showAndWait()
        }
    }

    fun showFailedToCreate() {
        launchOnFxThread {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = LanguageUtils.getString("network_error")
            alert.headerText = null
            alert.contentText = LanguageUtils.getString("failed_to_create_data")
            alert.showAndWait()
        }
    }

    fun showFailedToSave() {
        launchOnFxThread {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = LanguageUtils.getString("network_error")
            alert.headerText = null
            alert.contentText = LanguageUtils.getString("failed_to_save_data")
            alert.showAndWait()
        }
    }

    fun showFailedToDelete() {
        launchOnFxThread {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = LanguageUtils.getString("network_error")
            alert.headerText = null
            alert.contentText = LanguageUtils.getString("failed_to_delete_data")
            alert.showAndWait()
        }
    }

    fun deleteConfirmation(): Boolean {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = LanguageUtils.getString("delete_item")
        alert.headerText = LanguageUtils.getString("delete_confirmation")
        val result = alert.showAndWait()
        return result.get() == ButtonType.OK
    }
}