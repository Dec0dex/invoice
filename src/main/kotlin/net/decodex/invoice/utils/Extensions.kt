package net.decodex.invoice.utils

import javafx.scene.control.ChoiceBox
import javafx.scene.control.TableView
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

fun <T> TableView<T>.onChangeListener(listener: (T) -> Unit) {
    this.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
        listener(this.items[newValue.toInt()])
    }
}

public fun Any.launchOnFxThread(
    block: suspend CoroutineScope.() -> Unit
): Job {
    return GlobalScope.launch(Dispatchers.JavaFx) {
        block()
    }
}