package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.text.Text
import javafx.stage.Stage
import net.decodex.invoice.domain.model.NavigationModel
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils
import java.net.URL
import java.util.*


class SettingsViewController : Initializable {

    @FXML
    private lateinit var navigationView: TreeView<NavigationModel>

    @FXML
    private lateinit var navigationLabel: Label

    @FXML
    private lateinit var contentPane: BorderPane

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        navigationView.addEventHandler(MouseEvent.MOUSE_CLICKED) { event -> handleMouseClicked(event) }
        navigationView.isShowRoot = false
        navigationView.root = createRootTreeItem()
        navigationView.root.children.add(createCompanyTreeItem())
        navigationView.root.children.add(createUnitOfMeasureTreeItem())
        navigationView.root.children.add(createLanguageTreeItem())
    }

    @FXML
    fun saveSettings() {
        (navigationView.scene.window as Stage).close()
    }

    private fun createRootTreeItem(): TreeItem<NavigationModel> {
        val rootItem = TreeItem(
            NavigationModel(
                LanguageUtils.getString(
                    "settings"
                ), ""
            )
        )
        rootItem.isExpanded = true
        return rootItem
    }

    private fun createLanguageTreeItem(): TreeItem<NavigationModel> {
        return TreeItem(
            NavigationModel(
                LanguageUtils.getString(
                    "language"
                ), "settings_language.fxml"
            )
        )
    }

    private fun createCompanyTreeItem(): TreeItem<NavigationModel> {
        return TreeItem(
            NavigationModel(
                LanguageUtils.getString(
                    "company"
                ), "settings_company.fxml"
            )
        )
    }

    private fun createUnitOfMeasureTreeItem(): TreeItem<NavigationModel> {
        return TreeItem(
            NavigationModel(
                LanguageUtils.getString(
                    "units_of_measure"
                ), "settings_uom.fxml"
            )
        )
    }

    private fun handleMouseClicked(event: MouseEvent) {
        val node: Node = event.pickResult.intersectedNode

        if (node is Text || node is TreeCell<*> && node.text != null) {
            val selectedItem = navigationView.selectionModel.selectedItem
            navigationLabel.text = getNodePath(selectedItem)
            contentPane.center = FXLoader.load(selectedItem.value.viewFile)
        }
    }

    private fun getNodePath(treeItem: TreeItem<NavigationModel>): String {
        val pathBuilder: StringBuilder = StringBuilder()
        var item: TreeItem<NavigationModel>? = treeItem
        while (item != null) {
            pathBuilder.insert(0, item.value)
            pathBuilder.insert(0, " > ")
            item = item.parent
        }
        return pathBuilder.toString().removePrefix(" > ")
    }
}