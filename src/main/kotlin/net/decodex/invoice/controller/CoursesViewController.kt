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
import net.decodex.invoice.domain.model.course.Course
import net.decodex.invoice.utils.AlertUtils
import net.decodex.invoice.utils.Cache
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.launchOnFxThread
import net.decodex.invoice.view.CourseDialog
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class CoursesViewController : Initializable {

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
    private lateinit var coursesTableView: TableView<Course>

    @FXML
    private lateinit var idColumn: TableColumn<Long, Course>

    @FXML
    private lateinit var nameColumn: TableColumn<String, Course>

    @FXML
    private lateinit var descriptionColumn: TableColumn<String, Course>

    @FXML
    private lateinit var certificateColumn: TableColumn<String, Course>

    @FXML
    private lateinit var recordBookColumn: TableColumn<String, Course>

    @FXML
    private lateinit var priceColumn: TableColumn<Double, Course>

    @FXML
    private fun search() {
        val result = coursesTableView.items.find {
            it.description.toLowerCase().contains(searchText.text.toLowerCase())  ||
                    it.name.toLowerCase().contains(searchText.text.toLowerCase()) ||
                    it.certificateField.toLowerCase().contains(searchText.text.toLowerCase()) ||
                    it.recordBookField.toLowerCase().contains(searchText.text.toLowerCase())
        }
        result?.let {
            coursesTableView.selectionModel.select(it)
            coursesTableView.scrollTo(it)
        }
    }

    @FXML
    private fun add() {
        val result = CourseDialog.newInstance().showAndWait()
        if (result.isPresent) {
            addItem(result.get())
        }
    }

    @FXML
    private fun edit() {
        val result = CourseDialog.newInstance(getSelectedItem()).showAndWait()
        if (result.isPresent) {
            editItem(result.get())
        }
    }

    @FXML
    private fun delete() {
        if (AlertUtils.deleteConfirmation()) {
            deleteItem(coursesTableView.selectionModel.selectedItem)
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
        descriptionColumn.cellValueFactory = PropertyValueFactory("description")
        certificateColumn.cellValueFactory = PropertyValueFactory("certificateField")
        recordBookColumn.cellValueFactory = PropertyValueFactory("recordBookField")
        priceColumn.cellValueFactory = PropertyValueFactory("price")
    }

    private fun bindTableViewSelection() {
        coursesTableView.isEditable = false
        coursesTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        editButton.disableProperty().bind(Bindings.isEmpty(coursesTableView.selectionModel.selectedItems))
        deleteButton.disableProperty().bind(Bindings.isEmpty(coursesTableView.selectionModel.selectedItems))
        searchText.textProperty().addListener { _ -> search() }
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
                MainView.instance.controler.setStatusText(LanguageUtils.getString("fetching_courses"))
                val result = FXCollections.observableArrayList(Api.courseApi.findAllForCompanyId(Cache.user.companyId))
                launchOnFxThread { coursesTableView.items = result }
            } catch (ex: Exception) {
                AlertUtils.showFailedToLoadData()
                LOG.error("Failed to load course data", ex)
            } finally {
                MainView.instance.controler.resetStatus()
            }
        }
    }

    private fun addItem(course: Course) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("creating_course"))
                Api.courseApi.create(course)
            } catch (ex: Exception) {
                AlertUtils.showFailedToCreate()
                LOG.error("Failed to create course", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun editItem(course: Course) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("updating_course"))
                Api.courseApi.update(course)
            } catch (ex: Exception) {
                AlertUtils.showFailedToSave()
                LOG.error("Failed to save course", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    private fun deleteItem(course: Course) {
        GlobalScope.launch {
            try {
                MainView.instance.controler.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
                MainView.instance.controler.setStatusText(LanguageUtils.getString("deleting_course"))
                Api.courseApi.delete(course.id)
            } catch (ex: Exception) {
                AlertUtils.showFailedToDelete()
                LOG.error("Failed to delete course", ex)
            } finally {
                MainView.instance.controler.resetStatus()
                initializeData()
            }
        }
    }

    fun getSelectedItem(): Course? {
        return coursesTableView.selectionModel.selectedItem
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}