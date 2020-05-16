package net.decodex.invoice.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Invoice(
    var number: Int,
    var name: String,
    var dateCreated: Date,
    var dateOfTraffic: Date,
    var paymentDue: Date,
    var client: Client,
    var company: Company,
    var sum: Double,
    var payedAmount: Double,
    var remainingAmount: Double,
    var id: Long,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
) {
    fun getClientText(): String {
        return "${client.name}; ${client.pib}; ${client.address} ${client.postalCode} ${client.city}; ${client.registrationNumber}"
    }

    fun getDateCreatedText(): String {
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(dateCreated)
    }

    fun getDateOfTrafficText(): String {
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(dateOfTraffic)
    }

    fun getPaymentDueText(): String {
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(paymentDue)
    }
}