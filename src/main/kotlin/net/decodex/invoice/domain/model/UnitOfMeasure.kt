package net.decodex.invoice.domain.model

data class UnitOfMeasure(
    var name: String,
    var id: Long,
    var companyId: Long
) {
    override fun toString(): String {
        return name
    }
}