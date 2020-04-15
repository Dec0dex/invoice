package net.decodex.invoice.domain.dto

import java.util.*

class CreateInvoiceDto(
    var clientId: Long,
    var companyId: Long,
    var dateCreated: Date,
    var dateOfTraffic: Date,
    var paymentDue: Date,
    var id: Long = 0
) {}