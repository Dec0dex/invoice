package net.decodex.invoice.model

import javax.swing.text.View

data class NavigationModel(
    val title: String,
    val viewFile: String
) {
    override fun toString(): String {
        return title
    }
}