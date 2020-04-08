package net.decodex.invoice.domain.model

data class NavigationModel(
    val title: String,
    val viewFile: String
) {
    override fun toString(): String {
        return title
    }
}