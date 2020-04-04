package rs.decodex.invoice.model

import java.util.*

data class User(
    val username: String,
    var password: String,
    var fullName: String,
    var dateOfBirth: Date? = null
)