package net.decodex.invoice.view

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import net.decodex.invoice.controller.InvoiceProductDialogController
import net.decodex.invoice.domain.dto.CreateInvoiceProductDto
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils

class InvoiceProductDialog private constructor(clientId: Long, private val product: CreateInvoiceProductDto?) :
    Dialog<CreateInvoiceProductDto>() {
    private var controller: InvoiceProductDialogController

    init {
        val result = FXLoader.load<InvoiceProductDialogController>("dialog_invoice_product.fxml")
        controller = result.second
        controller.clientId = clientId
        setResultConverter(resultConverter())
        dialogPane.content = result.first
        dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL);
        if (product == null) {
            title = LanguageUtils.getString("add_item")
        } else {
            title = LanguageUtils.getString("edit_item")
            Platform.runLater { controller.fillTextFieldsWithData(product) }
        }
    }

    private fun resultConverter(): (param: ButtonType) -> CreateInvoiceProductDto? {
        return { dialogButton ->
            if (dialogButton == ButtonType.OK) {
                if (product == null) {
                    controller.createNewInvoiceProduct()
                } else {
                    controller.getUpdatedInvoiceProduct()
                }
            } else {
                null
            }
        }
    }

    companion object {
        fun newInstance(clientId: Long, product: CreateInvoiceProductDto? = null): InvoiceProductDialog {
            return InvoiceProductDialog(clientId, product)
        }
    }
}