package net.decodex.invoice

import javafx.application.Application
import javafx.stage.Stage
import net.decodex.invoice.view.LoginView
import net.decodex.invoice.view.MainView

class App : Application() {

    override fun start(p0: Stage?) {
        MainView.newInstance().show()
        LoginView.newInstance().showAndWait()
    }

    companion object {
        fun main(args: Array<String>) {
            launch(*args)
        }
    }
}
