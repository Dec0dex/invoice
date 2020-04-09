package net.decodex.invoice.controller

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import java.net.URL
import java.util.*

class ClientsViewController: Initializable {

    @FXML private lateinit var addButton: Button
    @FXML private lateinit var editButton: Button
    @FXML private lateinit var deleteButton: Button
    @FXML private lateinit var searchButton: Button
    @FXML private lateinit var searchText: TextField
    @FXML private lateinit var clientsTableView: TableView<Any>

    @FXML
    private fun search() {
        TODO("Implement")
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setButtonIcons()
        bindTableViewSelection()
    }

    private fun bindTableViewSelection() {
        clientsTableView.selectionModel.selectionMode = SelectionMode.SINGLE
        editButton.disableProperty().bind(Bindings.isEmpty(clientsTableView.selectionModel.selectedItems))
        deleteButton.disableProperty().bind(Bindings.isEmpty(clientsTableView.selectionModel.selectedItems))
    }

    private fun setButtonIcons() {
        GlyphsDude.setIcon(addButton, FontAwesomeIcon.PLUS, "16")
        GlyphsDude.setIcon(editButton, FontAwesomeIcon.PENCIL, "16")
        GlyphsDude.setIcon(deleteButton, FontAwesomeIcon.MINUS_CIRCLE, "16")
        GlyphsDude.setIcon(searchButton, FontAwesomeIcon.SEARCH, "16")
    }
}