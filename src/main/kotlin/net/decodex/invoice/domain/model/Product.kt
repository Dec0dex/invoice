package net.decodex.invoice.domain.model

import org.slf4j.LoggerFactory
import java.util.*

data class Product(
    var id: Long,
    var name: String,
    var pdv: Int,
    var unitOfMeasure: UnitOfMeasure,
    var barcode: String? = null,
    var companyId: Long? = 0,
    var price: Double? = 0.0,
    var createdAt: Date? = Date(),
    var updatedAt: Date? = Date(),
    var number: Int? = 0,
    var discount: Int? = 0,
    var quantity: Int? = 0,
    var priceId: Long? = 0,
    var value: Double = 0.0,
    var taxValue: Double = 0.0,
    var valueWithTax: Double = 0.0
) {

    fun calculateValues() {
        val basePrice = price!! * quantity!!
        val discount = basePrice * (discount ?: 0 / 100)
        value = basePrice - discount

        taxValue = value * (pdv / 100.0)
        valueWithTax = value + taxValue
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}