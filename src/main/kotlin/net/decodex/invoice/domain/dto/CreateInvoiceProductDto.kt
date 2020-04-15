package net.decodex.invoice.domain.dto

class CreateInvoiceProductDto(
    var productId: Long,
    var price: Double,
    var discount: Int,
    var quantity: Int,

    var id: Long = 0
)