package net.decodex.invoice.domain.model

data class Company(
    var id: Long,
    var name: String,
    var address: String,
    var city: String,
    var postalCode: String,
    var pib: String,
    var registrationNumber: String,
    var accountNumber: String,
    var phoneNumber: String? = null,
    var email: String? = null
)