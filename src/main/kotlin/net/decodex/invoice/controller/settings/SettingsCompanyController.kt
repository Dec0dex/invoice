package net.decodex.invoice.controller.settings

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS
import javafx.scene.control.TextField
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.decodex.invoice.api.Api
import net.decodex.invoice.domain.model.Company
import net.decodex.invoice.utils.AlertUtils
import net.decodex.invoice.utils.Cache
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.launchOnFxThread
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*

/**
 * TODO: Validation of the fields
 */
class SettingsCompanyController : Initializable {

    @FXML
    private lateinit var name: TextField

    @FXML
    private lateinit var city: TextField

    @FXML
    private lateinit var address: TextField

    @FXML
    private lateinit var postalCode: TextField

    @FXML
    private lateinit var pib: TextField

    @FXML
    private lateinit var accountNumber: TextField

    @FXML
    private lateinit var phoneNumber: TextField

    @FXML
    private lateinit var email: TextField

    private lateinit var company: Company

    @FXML
    private fun saveChanges() {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("saving_company_info"))
                val result = Api.companyApi.updateCompany(
                    Company(
                        company.id,
                        name.text,
                        address.text,
                        city.text,
                        postalCode.text,
                        pib.text,
                        accountNumber.text,
                        phoneNumber.text,
                        email.text
                    )
                )
                LOG.info(result.toString())
            } catch (ex: Exception) {
                LOG.error("Failed to save company info", ex)
                AlertUtils.showFailedToSave()
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_company_info"))
                val companyInfo = Api.companyApi.getCompanyInfo(Cache.user.companyId)
                fillTextFieldsWithData(companyInfo)
            } catch (e: Exception) {
                LOG.error("Failed to fetch company info", e)
                AlertUtils.showFailedToLoadData()
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    private fun fillTextFieldsWithData(company: Company) {
        this.company = company
        launchOnFxThread {
            name.text = company.name
            city.text = company.city
            postalCode.text = company.postalCode
            pib.text = company.pib
            accountNumber.text = company.accountNumber
            phoneNumber.text = company.phoneNumber
            email.text = company.email
            address.text = company.address
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}