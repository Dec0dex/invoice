package net.decodex.invoice.view

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import net.decodex.invoice.controller.InvoiceDialogController
import net.decodex.invoice.domain.dto.CreateInvoiceDto
import net.decodex.invoice.domain.dto.CreateInvoiceProductDto
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils

class InvoiceDialog private constructor(private val invoice: CreateInvoiceDto?) :
    Dialog<CreateInvoiceDto>() {
    private var controller: InvoiceDialogController

    init {
        val result = FXLoader.load<InvoiceDialogController>("dialog_invoice.fxml")
        controller = result.second
        setResultConverter(resultConverter())
        dialogPane.content = result.first
        dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL);
        if (invoice == null) {
            title = LanguageUtils.getString("add_item")
        } else {
            title = LanguageUtils.getString("edit_item")
            Platform.runLater { controller.fillTextFieldsWithData(invoice) }
        }
    }

    private fun resultConverter(): (param: ButtonType) -> CreateInvoiceDto? {
        return { dialogButton ->
            if (dialogButton == ButtonType.OK) {
                if (invoice == null) {
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
        fun newInstance(invoice: CreateInvoiceDto? = null): InvoiceDialog {
            return InvoiceDialog(invoice)
        }
    }
}