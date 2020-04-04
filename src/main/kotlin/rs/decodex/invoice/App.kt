package rs.decodex.invoice

import javafx.application.Application
import javafx.stage.Stage
import rs.decodex.invoice.view.LoginView
import rs.decodex.invoice.view.MainView

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
