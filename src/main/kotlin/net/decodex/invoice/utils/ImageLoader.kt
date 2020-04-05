package net.decodex.invoice.utils

import javafx.scene.image.Image

object ImageLoader {
    fun load(image: String): Image {
        return Image(javaClass.getResourceAsStream("/images/$image"))
    }
}