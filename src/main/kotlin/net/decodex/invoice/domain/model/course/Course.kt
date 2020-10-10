package net.decodex.invoice.domain.model.course

import java.util.*

data class Course(
    var name: String,
    var description: String,
    var certificateField: String,
    var recordBookField: String,
    var price: Double,
    var id: Long,
    var companyId: Long? = 0,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
)