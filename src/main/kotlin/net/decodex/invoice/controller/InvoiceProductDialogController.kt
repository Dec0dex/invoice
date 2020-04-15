package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.decodex.invoice.api.Api
import net.decodex.invoice.domain.dto.CreateInvoiceProductDto
import net.decodex.invoice.domain.model.Product
import net.decodex.invoice.utils.AlertUtils
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.ValidationUtils
import net.decodex.invoice.utils.launchOnFxThread
import net.decodex.invoice.view.MainView
import net.decodex.invoice.view.ProductSearchDialog
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*

class InvoiceProductDialogController : Initializable {

    @FXML
    private lateinit var productName: TextField

    @FXML
    private lateinit var price: TextField

    @FXML
    private lateinit var discount: TextField

    @FXML
    private lateinit var quantity: TextField

    var product: CreateInvoiceProductDto? = null
    var clientId: Long = 0
    private var productId: Long = 0

    @FXML
    private fun selectProduct() {
        val result = ProductSearchDialog.newInstance().showAndWait()
        if (result.isPresent) {
            productId = result.get().id
            productName.text = result.get().name
            price.text = result.get().price.toString()
            autopopulatePrice()
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        ValidationUtils.addDoubleValidation(price)
        ValidationUtils.addIntValidation(discount)
        ValidationUtils.addIntValidation(quantity)
        discount.text = "0"
        quantity.text = "1"
    }

    fun fillTextFieldsWithData(product: CreateInvoiceProductDto) {
        this.product = product
        this.productId = product.productId
        launchOnFxThread {
            productName.text = getProductInfoAsync(product.productId).await()?.name
            price.text = product.price.toString()
            discount.text = product.discount.toString()
            quantity.text = product.quantity.toString()
        }
    }

    fun getUpdatedInvoiceProduct(): CreateInvoiceProductDto {
        product?.let {
            it.productId = productId
            it.price = price.text.toDouble()
            it.discount = discount.text.toInt()
            it.quantity = quantity.text.toInt()
        }
        return product!!
    }

    fun createNewInvoiceProduct(): CreateInvoiceProductDto {
        return CreateInvoiceProductDto(
            productId,
            price.text.toDouble(),
            discount.text.toInt(),
            quantity.text.toInt()
        )
    }

    private suspend fun getProductInfoAsync(productId: Long): Deferred<Product?> {
        try {
            MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
            MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_products"))
            return GlobalScope.async {
                Api.productApi.findById(productId)
            }
        } catch (ex: Exception) {
            AlertUtils.showFailedToLoadData()
            LOG.error("Failed to fetch product data", ex)
        } finally {
            MainView.instance.controler.resetStatus()
        }

        return GlobalScope.async {
            null
        }
    }

    private fun autopopulatePrice() {
        GlobalScope.launch {
            try {
                val clientPrice = Api.productApi.getProductPriceForClient(productId, clientId)
                launchOnFxThread { price.text = clientPrice.toString() }
            } catch (ex: Exception) {
                LOG.warn("Failed to fetch product price for client", ex)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}