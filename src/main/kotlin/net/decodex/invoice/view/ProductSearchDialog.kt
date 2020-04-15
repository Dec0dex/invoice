package net.decodex.invoice.view

import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import net.decodex.invoice.controller.ProductsViewController
import net.decodex.invoice.domain.model.Product
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils

class ProductSearchDialog private constructor() : Dialog<Product>() {
    private var controller: ProductsViewController

    init {
        val result = FXLoader.load<ProductsViewController>("view_products.fxml")
        controller = result.second
        setResultConverter(resultConverter())
        dialogPane.content = result.first
        dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL);
        title = LanguageUtils.getString("select_product")
    }

    private fun resultConverter(): (param: ButtonType) -> Product? {
        return { dialogButton ->
            if (dialogButton == ButtonType.OK) {
                controller.getSelectedItem()
            } else {
                null
            }
        }
    }

    companion object {
        fun newInstance(): ProductSearchDialog {
            return ProductSearchDialog()
        }
    }
}