package net.decodex.invoice.controller

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.decodex.invoice.api.Api
import net.decodex.invoice.domain.model.Product
import net.decodex.invoice.domain.model.UnitOfMeasure
import net.decodex.invoice.utils.*
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*

class ProductsDialogController : Initializable {

    @FXML
    private lateinit var name: TextField

    @FXML
    private lateinit var barcode: TextField

    @FXML
    private lateinit var unitOfMeasure: ChoiceBox<UnitOfMeasure>

    @FXML
    private lateinit var pdv: TextField

    @FXML
    private lateinit var price: TextField

    var product: Product? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initializeFields()
        pdv.text = PropUtils.load("defaultPdv")
        price.text = "0.0"
        name.requestFocus()
        initializeData()
    }

    private fun initializeFields() {
        ValidationUtils.addDoubleValidation(price)
        ValidationUtils.addIntValidation(pdv)
    }

    private fun initializeData() {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_units_of_measure"))
                val result = FXCollections.observableArrayList(Api.unitOfMeasureApi.findAll(Cache.user.companyId))
                launchOnFxThread { unitOfMeasure.items = result }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load units of measure data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    fun fillTextFieldsWithData(product: Product) {
        this.product = product
        launchOnFxThread {
            name.text = product.name
            barcode.text = product.barcode
            unitOfMeasure.selectionModel.select(product.unitOfMeasure)
            pdv.text = product.pdv.toString()
            price.text = product.price.toString()
            name.requestFocus()
        }
    }

    fun getUpdated(): Product {
        product?.let {
            it.name = name.text
            it.pdv = pdv.text.toInt()
            it.unitOfMeasure = unitOfMeasure.selectionModel.selectedItem
            it.barcode = barcode.text
            it.price = price.text.toDouble()
        }
        return product!!
    }

    fun createNew(): Product {
        return Product(
            0,
            name.text,
            pdv.text.toInt(),
            unitOfMeasure.selectionModel.selectedItem,
            barcode.text,
            Cache.user.companyId,
            price.text.toDouble()
        )
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}