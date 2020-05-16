package net.decodex.invoice.domain.model

import java.util.*

data class Client(
    var id: Long,
    var companyId: Long,
    var name: String,
    var address: String,
    var city: String,
    var postalCode: String,
    var pib: String,
    var registrationNumber: String,
    var accountNumber: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
)