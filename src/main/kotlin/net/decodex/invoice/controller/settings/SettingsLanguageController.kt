package net.decodex.invoice.controller.settings

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ChoiceBox
import net.decodex.invoice.utils.LanguageUtils
import net.decodex.invoice.utils.onChangeListener
import java.net.URL
import java.util.*

class SettingsLanguageController : Initializable {

    @FXML
    private lateinit var languageChoice: ChoiceBox<Locale>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        languageChoice.items = FXCollections.observableArrayList()
        languageChoice.items.add(Locale("en", "US"))
        languageChoice.items.add(Locale("sr", "RS"))
        languageChoice.selectionModel.select(Locale.getDefault())
        languageChoice.onChangeListener { newLocale -> LanguageUtils.setLocale(newLocale) }
    }


}