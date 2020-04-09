package net.decodex.invoice.controller.settings

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.beans.binding.Bindings
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextInputDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.decodex.invoice.api.Api
import net.decodex.invoice.domain.model.UnitOfMeasure
import net.decodex.invoice.utils.AlertUtils
import net.decodex.invoice.utils.Cache
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.launchOnFxThread
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class SettingsUnitOfMeasureController : Initializable {

    @FXML
    lateinit var uomList: ListView<UnitOfMeasure>

    @FXML
    lateinit var addButton: Button

    @FXML
    lateinit var editButton: Button

    @FXML
    lateinit var deleteButton: Button

    @FXML
    fun add() {
        val dialog = TextInputDialog()
        dialog.title = LanguageUtils.getString("add_item")
        dialog.headerText = null
        dialog.contentText = LanguageUtils.getString("unit_of_measure")

        val result = dialog.showAndWait()
        if (result.isPresent) {
            addItem(UnitOfMeasure(result.get(), 0, Cache.user.companyId))
        }
    }

    @FXML
    fun edit() {
        val selectedItem = uomList.selectionModel.selectedItem
        selectedItem?.let {
            val dialog = TextInputDialog(it.name)
            dialog.title = LanguageUtils.getString("edit_item")
            dialog.headerText = null
            dialog.contentText = LanguageUtils.getString("unit_of_measure")

            val result = dialog.showAndWait()
            if (result.isPresent) {
                it.name = result.get()
                editItem(it)
            }
        }
    }

    @FXML
    fun delete() {
        val selectedItem = uomList.selectionModel.selectedItem
        selectedItem?.let {
            if (AlertUtils.deleteConfirmation()) {
                deleteItem(selectedItem)
            }
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initializeData()
        setButtonIcons()
        bindListViewFocus()
    }

    private fun bindListViewFocus() {
        uomList.selectionModel.selectionMode = SelectionMode.SINGLE
        editButton.disableProperty().bind(Bindings.isEmpty(uomList.selectionModel.selectedItems))
        deleteButton.disableProperty().bind(Bindings.isEmpty(uomList.selectionModel.selectedItems))
    }

    private fun setButtonIcons() {
        GlyphsDude.setIcon(addButton, FontAwesomeIcon.PLUS, "32")
        GlyphsDude.setIcon(editButton, FontAwesomeIcon.PENCIL, "32")
        GlyphsDude.setIcon(deleteButton, FontAwesomeIcon.MINUS_CIRCLE, "32")
    }

    private fun initializeData() {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_units_of_measure"))
                val result = FXCollections.observableArrayList(Api.unitOfMeasureApi.findAll(Cache.user.companyId))
                launchOnFxThread { uomList.items = result }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load unit of measure data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    private fun addItem(unitOfMeasure: UnitOfMeasure) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("creating_unit_of_measure"))
                Api.unitOfMeasureApi.createUnitOfMeasure(unitOfMeasure)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to create unit of measure", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun editItem(unitOfMeasure: UnitOfMeasure) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("updating_unit_of_measure"))
                Api.unitOfMeasureApi.updateUnitOfMeasure(unitOfMeasure)
            } catch (ex: Exception) {
                AlertUtils.showFailedToSave()
                LOG.error("Failed to save unit of measure", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun deleteItem(unitOfMeasure: UnitOfMeasure) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("deleting_unit_of_measure"))
                Api.unitOfMeasureApi.deleteUnitOfMeasure(unitOfMeasure.id)
            } catch (ex: Exception) {
                AlertUtils.showFailedToDelete()
                LOG.error("Failed to delete unit of measure", ex)
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