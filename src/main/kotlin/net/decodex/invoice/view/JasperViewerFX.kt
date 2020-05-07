package net.decodex.invoice.view

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.beans.property.SimpleIntegerProperty
import javafx.embed.swing.SwingFXUtils
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperPrintManager
import net.sf.jasperreports.engine.export.JRXlsExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.awt.image.BufferedImage
import java.io.File


/**
 * An simple approach to JasperViewer in JavaFX.
 *
 * @author Gustavo Fragoso
 * @version 3.3
 */
class JasperViewerFX : Dialog<Void?>() {
    private var btnPrint: Button? = null
    private var btnSave: Button? = null
    private var btnBackPage: Button? = null
    private var btnFirstPage: Button? = null
    private var btnNextPage: Button? = null
    private var btnLastPage: Button? = null
    private var btnZoomIn: Button? = null
    private var btnZoomOut: Button? = null
    private var report: ImageView? = null
    private var lblReportPages: Label? = null
    private val view: Stage? = null
    private var txtPage: TextField? = null
    private var jasperPrint: JasperPrint? = null
    private val currentPage: SimpleIntegerProperty
    private var imageHeight = 0
    private var imageWidth = 0
    private var reportPages = 0

    // ***********************************************
    // Scene and button actions
    // ***********************************************
    private fun createContentPane(): BorderPane {
        btnPrint = Button()
        btnSave = Button()
        btnBackPage = Button()
        btnFirstPage = Button()
        btnNextPage = Button()
        btnLastPage = Button()
        btnZoomIn = Button()
        btnZoomOut = Button()

        GlyphsDude.setIcon(btnPrint, FontAwesomeIcon.PRINT, "24")
        GlyphsDude.setIcon(btnSave, FontAwesomeIcon.SAVE, "24")
        GlyphsDude.setIcon(btnBackPage, FontAwesomeIcon.BACKWARD, "24")
        GlyphsDude.setIcon(btnFirstPage, FontAwesomeIcon.FAST_BACKWARD, "24")
        GlyphsDude.setIcon(btnNextPage, FontAwesomeIcon.FORWARD, "24")
        GlyphsDude.setIcon(btnLastPage, FontAwesomeIcon.FAST_FORWARD, "24")
        GlyphsDude.setIcon(btnZoomIn, FontAwesomeIcon.SEARCH_PLUS, "24")
        GlyphsDude.setIcon(btnZoomOut, FontAwesomeIcon.SEARCH_MINUS, "24")

        btnPrint!!.setPrefSize(30.0, 30.0)
        btnSave!!.setPrefSize(30.0, 30.0)
        btnBackPage!!.setPrefSize(30.0, 30.0)
        btnFirstPage!!.setPrefSize(30.0, 30.0)
        btnNextPage!!.setPrefSize(30.0, 30.0)
        btnLastPage!!.setPrefSize(30.0, 30.0)
        btnZoomIn!!.setPrefSize(30.0, 30.0)
        btnZoomOut!!.setPrefSize(30.0, 30.0)
        btnBackPage!!.onAction = EventHandler {
            renderPage(
                getCurrentPage() - 1
            )
        }
        btnFirstPage!!.onAction = EventHandler {
            renderPage(
                1
            )
        }
        btnNextPage!!.onAction = EventHandler {
            renderPage(
                getCurrentPage() + 1
            )
        }
        btnLastPage!!.onAction = EventHandler {
            renderPage(
                reportPages
            )
        }
        btnZoomIn!!.onAction = EventHandler {
            zoom(
                0.15
            )
        }
        btnZoomOut!!.onAction = EventHandler {
            zoom(
                -0.15
            )
        }
        printAction()
        saveAction()
        txtPage = TextField("1")
        txtPage!!.setPrefSize(40.0, 30.0)
        txtPage!!.onAction = EventHandler {
            try {
                val page = txtPage!!.text.toInt()
                renderPage(if (page in 1..reportPages) page else 1)
            } catch (e: NumberFormatException) {
                renderPage(1)
            }
        }
        lblReportPages = Label("/ 1")
        val menu = HBox(5.0)
        menu.alignment = Pos.CENTER
        menu.padding = Insets(5.0)
        menu.prefHeight = 50.0
        menu.children.addAll(
            btnPrint, btnSave, btnFirstPage, btnBackPage, txtPage,
            lblReportPages, btnNextPage, btnLastPage, btnZoomIn, btnZoomOut
        )

        // This imageview will preview the pdf inside scrollpane
        report = ImageView()
        report!!.fitHeight = imageHeight.toDouble()
        report!!.fitWidth = imageWidth.toDouble()

        // Centralizing the ImageView on Scrollpane
        val contentGroup = Group()
        contentGroup.children.add(report)
        val stack = StackPane(contentGroup)
        stack.alignment = Pos.CENTER
        stack.style = "-fx-background-color: gray"
        val scroll = ScrollPane(stack)
        scroll.isFitToWidth = true
        scroll.isFitToHeight = true
        val root = BorderPane()
        root.top = menu
        root.center = scroll
        root.setPrefSize(1000.0, 500.0)
        return root
    }
    // ***********************************************
    // Properties
    // ***********************************************
    /**
     * Set the currentPage property value
     * @param pageNumber Page number
     */
    private fun setCurrentPage(pageNumber: Int) {
        currentPage.set(pageNumber)
    }

    /**
     * Get the currentPage property value
     * @return Current page value
     */
    private fun getCurrentPage(): Int {
        return currentPage.get()
    }

    // ***********************************************
    // Button Action
    // ***********************************************
    private fun printAction() {
        btnPrint!!.onAction = EventHandler {
            try {
                JasperPrintManager.printReport(jasperPrint, true)
                close()
            } catch (ex: JRException) {
                ex.printStackTrace()
            }
        }
    }

    private fun saveAction() {
        btnSave!!.onAction = EventHandler {
            val pdf = FileChooser.ExtensionFilter("Portable Document Format", "*.pdf")
            val html = FileChooser.ExtensionFilter("HyperText Markup Language", "*.html")
            val xml = FileChooser.ExtensionFilter("eXtensible Markup Language", "*.xml")
            val xls = FileChooser.ExtensionFilter("Microsoft Excel 2007", "*.xls")
            val xlsx = FileChooser.ExtensionFilter("Microsoft Excel 2016", "*.xlsx")
            val chooser = FileChooser()
            chooser.title = "Save As"
            chooser.extensionFilters.addAll(pdf, html, xml, xls, xlsx)
            chooser.selectedExtensionFilter = pdf
            val file = chooser.showSaveDialog(view)
            if (file != null) {
                val selectedExtension =
                    chooser.selectedExtensionFilter.extensions
                exportTo(file, selectedExtension[0])
            }
        }
    }

    /**
     * When the user reach first or last page he cannot go forward or backward
     * @param pageNumber Page number
     */
    private fun disableUnnecessaryButtons(pageNumber: Int) {
        val isFirstPage = pageNumber == 1
        val isLastPage = pageNumber == reportPages
        btnBackPage!!.isDisable = isFirstPage
        btnFirstPage!!.isDisable = isFirstPage
        btnNextPage!!.isDisable = isLastPage
        btnLastPage!!.isDisable = isLastPage
    }
    // ***********************************************
    // Export Utilities
    // ***********************************************
    /**
     * Choose the right export method for each file extension
     * @param file File
     * @param extension File extension
     */
    private fun exportTo(file: File, extension: String) {
        when (extension) {
            "*.pdf" -> exportToPdf(file)
            "*.html" -> exportToHtml(file)
            "*.xml" -> exportToXml(file)
            "*.xls" -> exportToXls(file)
            "*.xlsx" -> exportToXlsx(file)
            else -> exportToPdf(file)
        }
    }

    /**
     * Export report to html file
     */
    private fun exportToHtml(file: File) {
        try {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, file.path)
        } catch (ex: JRException) {
            ex.printStackTrace()
        }
    }

    /**
     * Export report to Pdf file
     */
    private fun exportToPdf(file: File) {
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.path)
        } catch (ex: JRException) {
            ex.printStackTrace()
        }
    }

    /**
     * Export report to old Microsoft Excel file
     */
    private fun exportToXls(file: File?) {
        try {
            val exporter = JRXlsExporter()
            exporter.setExporterInput(SimpleExporterInput(jasperPrint))
            exporter.exporterOutput = SimpleOutputStreamExporterOutput(file)
            exporter.exportReport()
        } catch (ex: JRException) {
            ex.printStackTrace()
        }
    }

    /**
     * Export report to Microsoft Excel file
     */
    private fun exportToXlsx(file: File?) {
        try {
            val exporter = JRXlsxExporter()
            exporter.setExporterInput(SimpleExporterInput(jasperPrint))
            exporter.exporterOutput = SimpleOutputStreamExporterOutput(file)
            exporter.exportReport()
        } catch (ex: JRException) {
            ex.printStackTrace()
        }
    }

    /**
     * Export report to XML file
     */
    private fun exportToXml(file: File) {
        try {
            JasperExportManager.exportReportToXmlFile(jasperPrint, file.path, false)
        } catch (ex: JRException) {
            ex.printStackTrace()
        }
    }
    // ***********************************************
    // Image related methods
    // ***********************************************
    /**
     * Renderize page to image
     * @param pageNumber Page number
     * @throws JRException
     */
    private fun pageToImage(pageNumber: Int): Image? {
        try {
            val zoom = 1.33.toFloat()
            val image =
                JasperPrintManager.printPageToImage(jasperPrint, pageNumber - 1, zoom) as BufferedImage
            val fxImage = WritableImage(imageHeight, imageWidth)
            return SwingFXUtils.toFXImage(image, fxImage)
        } catch (ex: JRException) {
            ex.printStackTrace()
        }
        return null
    }

    /**
     * Render specific page on screen
     * @param pageNumber
     */
    private fun renderPage(pageNumber: Int) {
        setCurrentPage(pageNumber)
        disableUnnecessaryButtons(pageNumber)
        txtPage!!.text = pageNumber.toString()
        report!!.image = pageToImage(pageNumber)
    }

    /**
     * Scale image from ImageView
     * @param factor Zoom factor
     */
    private fun zoom(factor: Double) {
        report!!.scaleX = report!!.scaleX + factor
        report!!.scaleY = report!!.scaleY + factor
        report!!.fitHeight = imageHeight + factor
        report!!.fitWidth = imageWidth + factor
    }

    /**
     * Load report from JasperPrint
     * @param title Dialog title
     * @param jasperPrint JasperPrint object
     */
    fun viewReport(title: String?, jasperPrint: JasperPrint) {
        this.jasperPrint = jasperPrint
        imageHeight = jasperPrint.pageHeight + 284
        imageWidth = jasperPrint.pageWidth + 201
        reportPages = jasperPrint.pages.size
        lblReportPages!!.text = "/ $reportPages"
        if (reportPages > 0) {
            renderPage(1)
        }
        setTitle(title)
        show()
    }

    init {
        initModality(Modality.WINDOW_MODAL)
        isResizable = true
        dialogPane = getDialogPane()
        dialogPane.content = createContentPane()
        dialogPane.buttonTypes.add(ButtonType.CLOSE)
        dialogPane.lookupButton(ButtonType.CLOSE).isVisible = false
        currentPage = SimpleIntegerProperty(this, "currentPage", 1)
    }
}