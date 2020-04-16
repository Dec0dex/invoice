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
import net.decodex.invoice.domain.dto.CreateInvoiceDto
import net.decodex.invoice.domain.dto.CreateInvoiceProductDto
import net.decodex.invoice.domain.model.Invoice
import net.decodex.invoice.domain.model.Product
import net.decodex.invoice.domain.model.UnitOfMeasure
import net.decodex.invoice.utils.*
import net.decodex.invoice.view.InvoiceDialog
import net.decodex.invoice.view.InvoiceProductDialog
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*

class InvoicesViewController : Initializable {

    private var lastSelectedInvoiceIndex: Int? = null

    @FXML
    private lateinit var searchText: TextField

    @FXML
    private lateinit var searchProductText: TextField

    @FXML
    private lateinit var searchButton: Button

    @FXML
    private lateinit var addButton: Button

    @FXML
    private lateinit var editButton: Button

    @FXML
    private lateinit var deleteButton: Button

    @FXML
    private lateinit var printButton: Button

    @FXML
    private lateinit var paymentButton: Button

    @FXML
    private lateinit var searchProductButton: Button

    @FXML
    private lateinit var addProductButton: Button

    @FXML
    private lateinit var editProductButton: Button

    @FXML
    private lateinit var deleteProductButton: Button

    @FXML
    private lateinit var invoiceTableView: TableView<Invoice>

    @FXML
    private lateinit var invoiceProductsTableView: TableView<Product>

    @FXML
    private lateinit var idColumn: TableColumn<Long, Invoice>

    @FXML
    private lateinit var nameColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var clientColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var dateCreatedColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var dateOfTrafficColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var paymentDueColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var valueColumn: TableColumn<Double, Invoice>

    @FXML
    private lateinit var payedValueColumn: TableColumn<Double, Invoice>

    @FXML
    private lateinit var remainingValueColumn: TableColumn<Double, Invoice>

    @FXML
    private lateinit var productNumberColumn: TableColumn<Int, Product>

    @FXML
    private lateinit var productNameColumn: TableColumn<String, Product>

    @FXML
    private lateinit var productUomColumn: TableColumn<UnitOfMeasure, Product>

    @FXML
    private lateinit var productQuantityColumn: TableColumn<Int, Product>

    @FXML
    private lateinit var productPriceColumn: TableColumn<Double, Product>

    @FXML
    private lateinit var productDiscountColumn: TableColumn<Int, Product>

    @FXML
    private lateinit var productWithDiscountValueColumn: TableColumn<Double, Product>

    @FXML
    private lateinit var productPdvColumn: TableColumn<Int, Product>

    @FXML
    private lateinit var productPdvValueColumn: TableColumn<Double, Product>

    @FXML
    private lateinit var productValueWithTaxColumn: TableColumn<Double, Product>

    @FXML
    private fun search() {
        val result = invoiceTableView.items.find {
            it.name.toLowerCase().contains(searchText.text.toLowerCase()) || it.getClientText().toLowerCase()
                .contains(searchText.text.toLowerCase())
        }
        result?.let {
            invoiceTableView.selectionModel.select(it)
            invoiceTableView.scrollTo(it)
        }
    }

    @FXML
    private fun add() {
        val result = InvoiceDialog.newInstance().showAndWait()
        if (result.isPresent) {
            addItem(result.get())
        }
    }

    @FXML
    private fun edit() {
        val selectedItem = invoiceTableView.selectionModel.selectedItem
        val invoice = CreateInvoiceDto(
            selectedItem.client.id,
            selectedItem.company.id,
            selectedItem.dateCreated,
            selectedItem.dateOfTraffic,
            selectedItem.paymentDue,
            selectedItem.id
        )
        val result = InvoiceDialog.newInstance(invoice).showAndWait()
        if (result.isPresent) {
            editItem(result.get())
        }
    }

    @FXML
    private fun delete() {
        if (AlertUtils.deleteConfirmation()) {
            deleteItem(invoiceTableView.selectionModel.selectedItem.id)
        }
    }

    @FXML
    private fun print() {
        val invoice = invoiceTableView.selectionModel.selectedItem
        ReportUtils.showReport("invoice_unsigned.jasper", invoiceProductsTableView.items, invoice)
    }

    @FXML
    private fun makePayment() {
        val dialog = TextInputDialog()
        dialog.title = LanguageUtils.getString("make_payment")
        dialog.headerText = null
        dialog.contentText = LanguageUtils.getString("value")

        val result = dialog.showAndWait()
        if (result.isPresent) {
            makePaymentViaApi(result)
        }
    }

    @FXML
    private fun searchProduct() {
        val result = invoiceProductsTableView.items.find {
            it.name.toLowerCase().contains(searchProductText.text.toLowerCase())
        }
        result?.let {
            invoiceProductsTableView.selectionModel.select(it)
            invoiceProductsTableView.scrollTo(it)
        }
    }

    @FXML
    private fun addProduct() {
        val selectedInvoice = invoiceTableView.selectionModel.selectedItem
        val result = InvoiceProductDialog.newInstance(selectedInvoice.client.id).showAndWait()
        if (result.isPresent) {
            addProductItem(selectedInvoice.id, result.get())
        }
    }

    @FXML
    private fun editProduct() {
        val selectedInvoice = invoiceTableView.selectionModel.selectedItem
        val selectedProduct = invoiceProductsTableView.selectionModel.selectedItem
        val dto = CreateInvoiceProductDto(
            selectedProduct.id,
            selectedProduct.price!!,
            selectedProduct.discount!!,
            selectedProduct.quantity!!,
            selectedProduct.priceId!!
        )
        val result = InvoiceProductDialog.newInstance(selectedInvoice.client.id, dto).showAndWait()
        if (result.isPresent) {
            editProductItem(selectedInvoice.id, result.get())
        }
    }

    @FXML
    private fun deleteProduct() {
        if (AlertUtils.deleteConfirmation()) {
            val selectedInvoice = invoiceTableView.selectionModel.selectedItem
            val selectedProduct = invoiceProductsTableView.selectionModel.selectedItem
            deleteProductItem(selectedInvoice.id, selectedProduct.priceId!!)
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setButtonIcons()
        bindTableViewSelection()
        setupTableColumns()
        initializeData()
    }

    private fun makePaymentViaApi(result: Optional<String>) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("updating_invoice"))
                val selectedItem = invoiceTableView.selectionModel.selectedItem
                Api.invoiceApi.makePayment(selectedItem.id, result.get().toDouble())
            } catch (ex: Exception) {
                LOG.error("Couldn't make payment", ex)
                AlertUtils.showFailedToSave()
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun bindTableViewSelection() {
        invoiceTableView.isEditable = false
        invoiceTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        editButton.disableProperty().bind(Bindings.isEmpty(invoiceTableView.selectionModel.selectedItems))
        deleteButton.disableProperty().bind(Bindings.isEmpty(invoiceTableView.selectionModel.selectedItems))
        printButton.disableProperty().bind(Bindings.isEmpty(invoiceTableView.selectionModel.selectedItems))
        paymentButton.disableProperty().bind(Bindings.isEmpty(invoiceTableView.selectionModel.selectedItems))
        addProductButton.disableProperty().bind(Bindings.isEmpty(invoiceTableView.selectionModel.selectedItems))
        searchText.textProperty().addListener { _ -> search() }
        invoiceTableView.onChangeListener {
            getInvoiceProducts(it.id)
            lastSelectedInvoiceIndex = invoiceTableView.selectionModel.selectedIndex
        }

        invoiceProductsTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        editProductButton.disableProperty()
            .bind(Bindings.isEmpty(invoiceProductsTableView.selectionModel.selectedItems))
        deleteProductButton.disableProperty()
            .bind(Bindings.isEmpty(invoiceProductsTableView.selectionModel.selectedItems))
        searchProductButton.textProperty().addListener { _ -> searchProduct() }
    }

    private fun setButtonIcons() {
        GlyphsDude.setIcon(addButton, FontAwesomeIcon.PLUS, "16")
        GlyphsDude.setIcon(editButton, FontAwesomeIcon.PENCIL, "16")
        GlyphsDude.setIcon(deleteButton, FontAwesomeIcon.MINUS_CIRCLE, "16")
        GlyphsDude.setIcon(searchButton, FontAwesomeIcon.SEARCH, "16")
        GlyphsDude.setIcon(printButton, FontAwesomeIcon.PRINT, "16")
        GlyphsDude.setIcon(paymentButton, FontAwesomeIcon.DOLLAR, "16")
        GlyphsDude.setIcon(addProductButton, FontAwesomeIcon.PLUS, "16")
        GlyphsDude.setIcon(editProductButton, FontAwesomeIcon.PENCIL, "16")
        GlyphsDude.setIcon(deleteProductButton, FontAwesomeIcon.MINUS_CIRCLE, "16")
        GlyphsDude.setIcon(searchProductButton, FontAwesomeIcon.SEARCH, "16")
    }

    private fun setupTableColumns() {
        //Invoice table
        idColumn.cellValueFactory = PropertyValueFactory("id")
        nameColumn.cellValueFactory = PropertyValueFactory("name")
        clientColumn.cellValueFactory = PropertyValueFactory("clientText")
        dateCreatedColumn.cellValueFactory = PropertyValueFactory("dateCreatedText")
        dateOfTrafficColumn.cellValueFactory = PropertyValueFactory("dateOfTrafficText")
        paymentDueColumn.cellValueFactory = PropertyValueFactory("paymentDueText")
        valueColumn.cellValueFactory = PropertyValueFactory("sum")
        payedValueColumn.cellValueFactory = PropertyValueFactory("payedAmount")
        remainingValueColumn.cellValueFactory = PropertyValueFactory("remainingAmount")

        //Products table
        productNumberColumn.cellValueFactory = PropertyValueFactory("number")
        productNameColumn.cellValueFactory = PropertyValueFactory("name")
        productUomColumn.cellValueFactory = PropertyValueFactory("unitOfMeasure")
        productQuantityColumn.cellValueFactory = PropertyValueFactory("quantity")
        productPriceColumn.cellValueFactory = PropertyValueFactory("price")
        productDiscountColumn.cellValueFactory = PropertyValueFactory("discount")
        productWithDiscountValueColumn.cellValueFactory = PropertyValueFactory("value")
        productPdvColumn.cellValueFactory = PropertyValueFactory("pdv")
        productPdvValueColumn.cellValueFactory = PropertyValueFactory("taxValue")
        productValueWithTaxColumn.cellValueFactory = PropertyValueFactory("valueWithTax")
    }

    private fun getInvoiceProducts(invoiceId: Long) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_invoice_products"))
                val result = FXCollections.observableArrayList(Api.invoiceApi.findAllProductsByInvoiceId(invoiceId))
                result.forEach { it.calculateValues() }
                launchOnFxThread { invoiceProductsTableView.items = result }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load invoice products data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    private fun initializeData() {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_invoices"))
                val result = FXCollections.observableArrayList(Api.invoiceApi.findAllForCompanyId(Cache.user.companyId))
                launchOnFxThread {
                    invoiceTableView.items = result
                    if (lastSelectedInvoiceIndex == null) {
                        invoiceTableView.scrollTo(result.lastIndex)
                    } else {
                        invoiceTableView.selectionModel.select(lastSelectedInvoiceIndex!!)
                        invoiceTableView.scrollTo(lastSelectedInvoiceIndex!!)
                    }
                }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load invoice data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    private fun addItem(dto: CreateInvoiceDto) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("creating_invoice"))
                Api.invoiceApi.create(dto)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to create invoice", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun editItem(dto: CreateInvoiceDto) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("updating_invoice"))
                Api.invoiceApi.update(dto)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to update invoice", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun deleteItem(invoiceId: Long) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("deleting_invoice"))
                Api.invoiceApi.delete(invoiceId)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to update delete", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun addProductItem(invoiceId: Long, dto: CreateInvoiceProductDto) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("creating_invoice_product"))
                Api.invoiceApi.createInvoiceProduct(invoiceId, dto)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to create invoice product", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun editProductItem(invoiceId: Long, dto: CreateInvoiceProductDto) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("updating_invoice_product"))
                Api.invoiceApi.updateInvoiceProduct(invoiceId, dto)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to update invoice product", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun deleteProductItem(invoiceId: Long, priceId: Long) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("deleting_invoice_product"))
                Api.invoiceApi.deleteInvoiceProduct(invoiceId, priceId)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to delete invoice product", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}