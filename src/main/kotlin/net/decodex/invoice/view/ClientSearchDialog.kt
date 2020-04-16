package net.decodex.invoice.view

import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import net.decodex.invoice.controller.ClientsViewController
import net.decodex.invoice.controller.ProductsViewController
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.domain.model.Product
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils

class ClientSearchDialog private constructor() : Dialog<Client>() {
    private var controller: ClientsViewController

    init {
        val result = FXLoader.load<ClientsViewController>("view_clients.fxml")
        controller = result.second
        setResultConverter(resultConverter())
        dialogPane.content = result.first
        dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL);
        title = LanguageUtils.getString("select_client")
    }

    private fun resultConverter(): (param: ButtonType) -> Client? {
        return { dialogButton ->
            if (dialogButton == ButtonType.OK) {
                controller.getSelectedItem()
            } else {
                null
            }
        }
    }

    companion object {
        fun newInstance(): ClientSearchDialog {
            return ClientSearchDialog()
        }
    }
}