package net.decodex.invoice.utils

import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.domain.model.Company
import net.decodex.invoice.domain.model.Invoice
import net.decodex.invoice.view.JasperViewerFX
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.util.JRLoader
import pl.allegro.finance.tradukisto.MoneyConverters
import java.math.BigDecimal


object ReportUtils {

    fun showReport(
        reportFile: String,
        data: List<Any>,
        invoice: Invoice,
        parameters: Map<String, Any> = generateParameters(invoice, data)
    ) {
        try {
            val jreport =
                JRLoader.loadObject(ReportUtils::class.java.getResource("/reports/$reportFile")) as JasperReport
            val jprint = JasperFillManager.fillReport(jreport, parameters, JREmptyDataSource())

            JasperViewerFX().viewReport(LanguageUtils.getString("app_name"), jprint)
        } catch (e: JRException) {
            e.printStackTrace()
        }
    }

    private fun generateParameters(invoice: Invoice, data: List<Any>): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        populateCompanyParameters(map, invoice.company)
        populateClientParameters(map, invoice.client)
        populateInvoiceParameters(map, invoice)
        map["DataSource"] = JRBeanCollectionDataSource(data)
        return map
    }

    private fun populateCompanyParameters(map: HashMap<String, Any>, company: Company) {
        map["COMPANY_NAME"] = company.name
        map["COMPANY_ADDRESS"] = company.address
        map["COMPANY_PHONE"] = company.phoneNumber ?: ""
        map["COMPANY_ACCOUNT_NUMBER"] = company.accountNumber
        map["COMPANY_PIB"] = company.pib
        map["COMPANY_REGISTRATION_NUMBER"] = company.registrationNumber
        map["COMPANY_EMAIL"] = company.email ?: "-/-"
        map["COMPANY_CITY"] = company.city
        map["COMPANY_POSTAL"] = company.postalCode
    }

    private fun populateClientParameters(map: HashMap<String, Any>, client: Client) {
        map["CLIENT_CITY"] = client.city
        map["CLIENT_EMAIL"] = client.email ?: ""
        map["CLIENT_ACCOUNT_NUMBER"] = client.accountNumber ?: ""
        map["CLIENT_PIB"] = client.pib
        map["CLIENT_REGISTRATION_NUMBER"] = client.registrationNumber
        map["CLIENT_POSTAL"] = client.postalCode
        map["CLIENT_ADDRESS"] = client.address
        map["CLIENT_NAME"] = client.name
    }

    private fun populateInvoiceParameters(map: HashMap<String, Any>, invoice: Invoice) {
        map["INVOICE_PAYMENT_DUE"] = invoice.getPaymentDueText()
        map["INVOICE_SUM"] = invoice.sum
        map["INVOICE_PAYED_AMOUNT"] = invoice.payedAmount
        map["INVOICE_REMAINING_AMOUNT"] = invoice.remainingAmount
        map["INVOICE_DATE_OF_TRAFFIC"] = invoice.getDateOfTrafficText()
        map["INVOICE_DATE_CREATED"] = invoice.getDateCreatedText()
        map["INVOICE_NAME"] = invoice.name
        map["INVOICE_REMAINING_AMOUNT_WORDS"] = convertValueToWords(invoice.remainingAmount)
    }

    private fun convertValueToWords(value: Double): String {
        val converter = MoneyConverters.SERBIAN_BANKING_MONEY_VALUE
        val result =  converter.asWords(BigDecimal(String.format("%.2f", value).replace(",", ".")))
        val split = result.split(" RSD ")
        return "${split.first().replace(" ", "")} RSD ${split.last()}"
    }
}