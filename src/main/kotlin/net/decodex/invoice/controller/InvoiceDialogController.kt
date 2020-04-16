package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.DatePicker
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import net.decodex.invoice.api.Api
import net.decodex.invoice.domain.dto.CreateInvoiceDto
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.utils.*
import net.decodex.invoice.view.ClientSearchDialog
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import java.net.URL
import java.time.LocalDate
import java.util.*

class InvoiceDialogController : Initializable {

    @FXML
    private lateinit var client: TextField

    @FXML
    private lateinit var dateCreated: DatePicker

    @FXML
    private lateinit var dateOfTraffic: DatePicker

    @FXML
    private lateinit var paymentDue: DatePicker

    var invoice: CreateInvoiceDto? = null
    private var clientId: Long = 0

    @FXML
    private fun selectClient() {
        val result = ClientSearchDialog.newInstance().showAndWait()
        if (result.isPresent) {
            clientId = result.get().id
            client.text = result.get().name
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        dateCreated.value = LocalDate.now()
        dateOfTraffic.value = LocalDate.now()
        paymentDue.value = LocalDate.now()
    }

    fun fillTextFieldsWithData(invoice: CreateInvoiceDto) {
        this.invoice = invoice
        clientId = invoice.clientId
        launchOnFxThread {
            client.text = getClientInfoAsync(invoice.clientId).await()?.name
            dateCreated.value = invoice.dateCreated.toLocaleDate()
            dateOfTraffic.value = invoice.dateOfTraffic.toLocaleDate()
            paymentDue.value = invoice.paymentDue.toLocaleDate()
        }
    }

    fun getUpdatedInvoiceProduct(): CreateInvoiceDto {
        invoice?.let {
            it.clientId = clientId
            it.dateCreated = dateCreated.value.toDate()
            it.dateOfTraffic = dateOfTraffic.value.toDate()
            it.paymentDue = paymentDue.value.toDate()
        }
        return invoice!!
    }

    fun createNewInvoiceProduct(): CreateInvoiceDto {
        return CreateInvoiceDto(
            clientId,
            Cache.user.companyId,
            dateCreated.value.toDate(),
            dateOfTraffic.value.toDate(),
            paymentDue.value.toDate()
        )
    }

    private suspend fun getClientInfoAsync(productId: Long): Deferred<Client?> {
        try {
            MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
            MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_clients"))
            return GlobalScope.async {
                Api.clientApi.findById(productId)
            }
        } catch (ex: Exception) {
            AlertUtils.showFailedToLoadData()
            LOG.error("Failed to fetch client data", ex)
        } finally {
            MainView.instance.controler.resetStatus()
        }

        return GlobalScope.async {
            null
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}