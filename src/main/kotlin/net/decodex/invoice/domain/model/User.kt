package net.decodex.invoice.domain.model

import java.util.*

data class User(
    var username: String,
    var password: String,
    var fullName: String,
    var email: String,
    var dateOfBirth: Date? = null,
    var companyId: Long = 0,
    var id: Long = 0,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)