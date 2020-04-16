package net.decodex.invoice

import javafx.application.Application
import javafx.stage.Stage
import net.decodex.invoice.utils.ReportUtils
import net.decodex.invoice.view.LoginView
import net.decodex.invoice.view.MainView
import org.slf4j.LoggerFactory
import javax.swing.LookAndFeel
import javax.swing.UIManager

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
