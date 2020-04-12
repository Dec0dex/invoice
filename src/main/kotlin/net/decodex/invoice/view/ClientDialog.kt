package net.decodex.invoice.view

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import net.decodex.invoice.controller.ClientsDialogController
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils

class ClientDialog private constructor(private val client: Client?) : Dialog<Client>() {
    private var controller: ClientsDialogController

    init {
        val result = FXLoader.load<ClientsDialogController>("dialog_clients.fxml")
        controller = result.second
        setResultConverter(resultConverter())
        dialogPane.content = result.first
        dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL);
        if (client == null) {
            title = LanguageUtils.getString("add_item")
        } else {
            title = LanguageUtils.getString("edit_item")
            Platform.runLater { controller.fillTextFieldsWithData(client) }
        }
    }

    private fun resultConverter(): (param: ButtonType) -> Client? {
        return { dialogButton ->
            if (dialogButton == ButtonType.OK) {
                if (client == null) {
                    controller.createNewClient()
                } else {
                    controller.getUpdatedClient()
                }
            } else {
                null
            }
        }
    }

    companion object {
        fun newInstance(client: Client? = null): ClientDialog {
            return ClientDialog(client)
        }
    }
}