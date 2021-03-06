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
import net.decodex.invoice.domain.model.Invoice
import net.decodex.invoice.utils.*
import net.decodex.invoice.view.ClientDialog
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class ClientsViewController : Initializable {

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
    private lateinit var clientsTableView: TableView<Client>

    @FXML
    private lateinit var invoiceTableView: TableView<Invoice>

    @FXML
    private lateinit var idColumn: TableColumn<Long, Client>

    @FXML
    private lateinit var nameColumn: TableColumn<String, Client>

    @FXML
    private lateinit var addressColumn: TableColumn<String, Client>

    @FXML
    private lateinit var postalCodeColumn: TableColumn<String, Client>

    @FXML
    private lateinit var cityColumn: TableColumn<String, Client>

    @FXML
    private lateinit var pibColumn: TableColumn<String, Client>

    @FXML
    private lateinit var registrationNumberColumn: TableColumn<String, Client>

    @FXML
    private lateinit var accountNumberColumn: TableColumn<String, Client>

    @FXML
    private lateinit var phoneNumberColumn: TableColumn<String, Client>

    @FXML
    private lateinit var emailColumn: TableColumn<String, Client>

    @FXML
    private lateinit var invoiceDateCreatedColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var invoiceDateOfTrafficColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var invoicePaymentDueColumn: TableColumn<String, Invoice>

    @FXML
    private lateinit var invoiceValueColumn: TableColumn<Double, Invoice>

    @FXML
    private lateinit var invoicePayedValueColumn: TableColumn<Double, Invoice>

    @FXML
    private lateinit var invoiceRemainingValueColumn: TableColumn<Double, Invoice>

    @FXML
    private lateinit var invoiceIdColumn: TableColumn<Long, Invoice>

    @FXML
    private lateinit var invoiceNameColumn: TableColumn<String, Invoice>

    @FXML
    private fun search() {
        val result = clientsTableView.items.find {
            it.pib.toLowerCase().contains(searchText.text.toLowerCase()) || it.name.toLowerCase()
                .contains(searchText.text.toLowerCase()) || it.registrationNumber.toLowerCase().contains(searchText.text.toLowerCase())
        }
        result?.let {
            clientsTableView.selectionModel.select(it)
            clientsTableView.scrollTo(it)
        }
    }

    @FXML
    private fun add() {
        val result = ClientDialog.newInstance().showAndWait()
        if (result.isPresent) {
            addItem(result.get())
        }
    }

    @FXML
    private fun edit() {
        val result = ClientDialog.newInstance(clientsTableView.selectionModel.selectedItem).showAndWait()
        if (result.isPresent) {
            editItem(result.get())
        }
    }

    @FXML
    private fun delete() {
        if (AlertUtils.deleteConfirmation()) {
            deleteItem(clientsTableView.selectionModel.selectedItem)
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
        addressColumn.cellValueFactory = PropertyValueFactory("address")
        postalCodeColumn.cellValueFactory = PropertyValueFactory("postalCode")
        cityColumn.cellValueFactory = PropertyValueFactory("city")
        pibColumn.cellValueFactory = PropertyValueFactory("pib")
        registrationNumberColumn.cellValueFactory = PropertyValueFactory("registrationNumber")
        accountNumberColumn.cellValueFactory = PropertyValueFactory("accountNumber")
        phoneNumberColumn.cellValueFactory = PropertyValueFactory("phoneNumber")
        emailColumn.cellValueFactory = PropertyValueFactory("email")

        //Invoice table
        invoiceIdColumn.cellValueFactory = PropertyValueFactory("id")
        invoiceNameColumn.cellValueFactory = PropertyValueFactory("name")
        invoiceDateCreatedColumn.cellValueFactory = PropertyValueFactory("dateCreatedText")
        invoiceDateOfTrafficColumn.cellValueFactory = PropertyValueFactory("dateOfTrafficText")
        invoicePaymentDueColumn.cellValueFactory = PropertyValueFactory("paymentDueText")
        invoiceValueColumn.cellValueFactory = PropertyValueFactory("sum")
        invoicePayedValueColumn.cellValueFactory = PropertyValueFactory("payedAmount")
        invoiceRemainingValueColumn.cellValueFactory = PropertyValueFactory("remainingAmount")
    }

    private fun bindTableViewSelection() {
        clientsTableView.isEditable = false
        clientsTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        invoiceTableView.isEditable = false
        editButton.disableProperty().bind(Bindings.isEmpty(clientsTableView.selectionModel.selectedItems))
        deleteButton.disableProperty().bind(Bindings.isEmpty(clientsTableView.selectionModel.selectedItems))
        searchText.textProperty().addListener { _-> search() }
        clientsTableView.onChangeListener { searchForClientDebts() }
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
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_clients"))
                val result = FXCollections.observableArrayList(Api.clientApi.findAllForCompanyId(Cache.user.companyId))
                launchOnFxThread { clientsTableView.items = result }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load clients data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    private fun addItem(client: Client) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("creating_client"))
                Api.clientApi.create(client)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to create Client", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun editItem(client: Client) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("updating_client"))
                Api.clientApi.update(client)
            } catch (ex: Exception) {
                AlertUtils.showFailedToSave()
                LOG.error("Failed to save client", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun deleteItem(client: Client) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("deleting_client"))
                Api.clientApi.delete(client.id)
            } catch (ex: Exception) {
                AlertUtils.showFailedToDelete()
                LOG.error("Failed to delete client", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun searchForClientDebts() {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_invoices"))
                val result = FXCollections.observableArrayList(Api.invoiceApi.findAllForCompanyIdWhichAreNotPayedForClientId(Cache.user.companyId, clientsTableView.selectionModel.selectedItem.id))
                launchOnFxThread { invoiceTableView.items = result }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load invoices data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    fun getSelectedItem(): Client? {
        return clientsTableView.selectionModel.selectedItem
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}