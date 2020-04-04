package rs.decodex.invoice.utils

import javafx.scene.control.ChoiceBox

fun <T> ChoiceBox<T>.onChangeListener(listener: (oldValue: T, newValue: T) -> Unit) {
    this.selectionModel.selectedIndexProperty().addListener { _, oldValue, newValue ->
        listener(this.items[oldValue.toInt()], this.items[newValue.toInt()])
    }
}

fun <T> ChoiceBox<T>.onChangeListener(listener: (T) -> Unit) {
    this.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
        listener(this.items[newValue.toInt()])
    }
}
