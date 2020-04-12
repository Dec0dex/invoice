package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.scene.control.TextField
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.utils.Cache
import net.decodex.invoice.utils.launchOnFxThread

class ClientsDialogController {

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

    var client: Client? = null

    fun fillTextFieldsWithData(client: Client) {
        this.client = client
        launchOnFxThread {
            name.text = client.name
            city.text = client.city
            postalCode.text = client.postalCode
            pib.text = client.pib
            accountNumber.text = client.accountNumber
            phoneNumber.text = client.phoneNumber
            email.text = client.email
            address.text = client.address
            name.requestFocus()
        }
    }

    fun getUpdatedClient(): Client {
        client?.let {
            it.name = name.text
            it.city = city.text
            it.postalCode = postalCode.text
            it.pib = pib.text
            it.accountNumber = accountNumber.text
            it.phoneNumber = phoneNumber.text
            it.email = email.text
            it.address = address.text
        }
        return client!!
    }

    fun createNewClient(): Client {
        return Client(
            0,
            Cache.user.companyId,
            name.text,
            address.text,
            city.text,
            postalCode.text,
            pib.text,
            accountNumber.text,
            phoneNumber.text,
            email.text
        )
    }
}