package net.decodex.invoice.controller

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.decodex.invoice.api.Api
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.domain.model.Product
import net.decodex.invoice.domain.model.UnitOfMeasure
import net.decodex.invoice.utils.AlertUtils
import net.decodex.invoice.utils.Cache
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.launchOnFxThread
import net.decodex.invoice.view.MainView
import net.decodex.invoice.view.ProductDialog
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class ProductsViewController : Initializable {

    @FXML
    private lateinit var addButton: Button

    @FXML
    private lateinit var editButton: Button

    @FXML
    private lateinit var deleteButton: Button

    @FXML
    private lateinit var searchButton: Button

    @FXML
    private lateinit var searchText: TextField

    @FXML
    private lateinit var productsTableView: TableView<Product>

    @FXML
    private lateinit var idColumn: TableColumn<Long, Product>

    @FXML
    private lateinit var nameColumn: TableColumn<String, Product>

    @FXML
    private lateinit var barcodeColumn: TableColumn<String, Product>

    @FXML
    private lateinit var unitOfMeasureColumn: TableColumn<UnitOfMeasure, Client>

    @FXML
    private lateinit var pdvColumn: TableColumn<Int, Client>

    @FXML
    private lateinit var priceColumn: TableColumn<Double, Client>

    @FXML
    private fun search() {
        val result = productsTableView.items.find {
            it.barcode?.toLowerCase()?.contains(searchText.text.toLowerCase())?:false || it.name.toLowerCase()
                .contains(searchText.text.toLowerCase())
        }
        result?.let {
            productsTableView.selectionModel.select(it)
            productsTableView.scrollTo(it)
        }
    }

    @FXML
    private fun add() {
        val result = ProductDialog.newInstance().showAndWait()
        if (result.isPresent) {
            addItem(result.get())
        }
    }

    @FXML
    private fun edit() {
        val result = ProductDialog.newInstance(productsTableView.selectionModel.selectedItem).showAndWait()
        if (result.isPresent) {
            editItem(result.get())
        }
    }

    @FXML
    private fun delete() {
        if (AlertUtils.deleteConfirmation()) {
            deleteItem(productsTableView.selectionModel.selectedItem)
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setButtonIcons()
        bindTableViewSelection()
        setupTableColumns()
        initializeData()
    }

    private fun setupTableColumns() {
        idColumn.cellValueFactory = PropertyValueFactory("id")
        nameColumn.cellValueFactory = PropertyValueFactory("name")
        barcodeColumn.cellValueFactory = PropertyValueFactory("barcode")
        unitOfMeasureColumn.cellValueFactory = PropertyValueFactory("unitOfMeasure")
        pdvColumn.cellValueFactory = PropertyValueFactory("pdv")
        priceColumn.cellValueFactory = PropertyValueFactory("price")
    }

    private fun bindTableViewSelection() {
        productsTableView.isEditable = false
        productsTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        editButton.disableProperty().bind(Bindings.isEmpty(productsTableView.selectionModel.selectedItems))
        deleteButton.disableProperty().bind(Bindings.isEmpty(productsTableView.selectionModel.selectedItems))
        searchText.textProperty().addListener { _-> search() }
    }

    private fun setButtonIcons() {
        GlyphsDude.setIcon(addButton, FontAwesomeIcon.PLUS, "16")
        GlyphsDude.setIcon(editButton, FontAwesomeIcon.PENCIL, "16")
        GlyphsDude.setIcon(deleteButton, FontAwesomeIcon.MINUS_CIRCLE, "16")
        GlyphsDude.setIcon(searchButton, FontAwesomeIcon.SEARCH, "16")
    }

    private fun initializeData() {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_products"))
                val result = FXCollections.observableArrayList(Api.productApi.findAllForCompanyId(Cache.user.companyId))
                launchOnFxThread { productsTableView.items = result }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load products data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    private fun addItem(product: Product) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("creating_product"))
                Api.productApi.create(product)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to create product", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun editItem(product: Product) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("updating_product"))
                Api.productApi.update(product)
            } catch (ex: Exception) {
                AlertUtils.showFailedToSave()
                LOG.error("Failed to save product", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun deleteItem(product: Product) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("deleting_product"))
                Api.productApi.delete(product.id)
            } catch (ex: Exception) {
                AlertUtils.showFailedToDelete()
                LOG.error("Failed to delete product", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    fun getSelectedItem(): Product? {
        return productsTableView.selectionModel.selectedItem
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}