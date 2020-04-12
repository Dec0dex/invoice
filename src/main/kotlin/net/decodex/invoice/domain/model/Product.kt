package net.decodex.invoice.domain.model

import java.util.*

data class Product(
    var id: Long,
    var name: String,
    var pdv: Int,
    var unitOfMeasure: UnitOfMeasure,
    var barcode: String? = null,
    var price: Double? = 0.0,
    var companyId: Long = 0,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
)