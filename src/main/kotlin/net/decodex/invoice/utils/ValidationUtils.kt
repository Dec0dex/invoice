package net.decodex.invoice.utils

import javafx.scene.control.TextField

object ValidationUtils {
    fun addDoubleValidation(textField: TextField) {
        textField.textProperty().addListener { _, oldValue, newValue ->
            if (newValue.matches(Regex("^[0-9]*(.)?([0-9]?)+\$"))) {
                textField.text = newValue
            } else {
                textField.text = oldValue
            }
        }
    }

    fun addIntValidation(textField: TextField) {
        textField.textProperty().addListener { _, oldValue, newValue ->
            if (newValue.matches(Regex("[0-9]*"))) {
                textField.text = newValue
            } else {
                textField.text = oldValue
            }
        }
    }
}