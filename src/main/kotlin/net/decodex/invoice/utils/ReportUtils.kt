package net.decodex.invoice.utils

import javafx.embed.swing.SwingNode
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.domain.model.Company
import net.decodex.invoice.domain.model.Invoice
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.util.JRLoader
import net.sf.jasperreports.swing.JRViewer
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException


object ReportUtils {

    fun showReport(
        reportFile: String,
        data: List<Any>,
        invoice: Invoice,
        parameters: Map<String, Any> = generateParameters(invoice)
    ) {
        try {
            val source = JRBeanCollectionDataSource(data)
            val jreport =
                JRLoader.loadObject(ReportUtils::class.java.getResource("/reports/$reportFile")) as JasperReport
            val jprint = JasperFillManager.fillReport(jreport, parameters, source)
            val swingNode = SwingNode()
            swingNode.content = JRViewer(jprint)

            val anchorPane = AnchorPane()

            AnchorPane.setTopAnchor(swingNode, 0.0)
            AnchorPane.setBottomAnchor(swingNode, 0.0)
            AnchorPane.setLeftAnchor(swingNode, 0.0)
            AnchorPane.setRightAnchor(swingNode, 0.0)

            anchorPane.children.add(swingNode)
            val scene = Scene(anchorPane)
            val stage = Stage()
            stage.height = 600.0
            stage.width = 800.0
            stage.isMaximized = true
            stage.isAlwaysOnTop = true
            stage.scene = scene
            setLookAndFeel()
            stage.showAndWait()
        } catch (e: JRException) {
            e.printStackTrace()
        }
    }

    private fun generateParameters(invoice: Invoice): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        populateCompanyParameters(map, invoice.company)
        return map
    }

    private fun populateCompanyParameters(map: HashMap<String, Any>, company: Company) {
        map["COMPANY_NAME"] = company.name
        map["COMPANY_ADDRESS"] = "${company.address}, ${company.postalCode} ${company.city}"
        map["COMPANY_PHONE"] = company.phoneNumber?: ""
        map["COMPANY_ACCOUNT_NUMBER"] = company.accountNumber
        map["COMPANY_PIB"] = company.pib
        map["COMPANY_EMAIL"] = company.email?:"-/-"
    }

    fun setLookAndFeel() {
        SwingUtilities.invokeLater {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: UnsupportedLookAndFeelException) {
                e.printStackTrace()
            }
        }
    }
}