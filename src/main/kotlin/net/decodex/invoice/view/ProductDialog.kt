package net.decodex.invoice.view

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import net.decodex.invoice.controller.ClientsDialogController
import net.decodex.invoice.controller.ProductsDialogController
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.domain.model.Product
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils

class ProductDialog private constructor(private val product: Product?) : Dialog<Product>() {
    private var controller: ProductsDialogController

    init {
        val result = FXLoader.load<ProductsDialogController>("dialog_products.fxml")
        controller = result.second
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

    private fun resultConverter(): (param: ButtonType) -> Product? {
        return { dialogButton ->
            if (dialogButton == ButtonType.OK) {
                if (product == null) {
                    controller.createNew()
                } else {
                    controller.getUpdated()
                }
            } else {
                null
            }
        }
    }

    companion object {
        fun newInstance(product: Product? = null): ProductDialog {
            return ProductDialog(product)
        }
    }
}