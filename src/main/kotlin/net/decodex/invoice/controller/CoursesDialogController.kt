package net.decodex.invoice.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import net.decodex.invoice.domain.model.course.Course
import net.decodex.invoice.utils.Cache
import net.decodex.invoice.utils.ValidationUtils
import net.decodex.invoice.utils.launchOnFxThread
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*

class CoursesDialogController : Initializable {

    @FXML
    private lateinit var name: TextField

    @FXML
    private lateinit var description: TextField

    @FXML
    private lateinit var certificate: TextField

    @FXML
    private lateinit var recordBook: TextField

    @FXML
    private lateinit var price: TextField

    var course: Course? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initializeFields()
        price.text = "0.0"
        name.requestFocus()
    }

    private fun initializeFields() {
        ValidationUtils.addDoubleValidation(price)
    }

    fun fillTextFieldsWithData(course: Course) {
        this.course = course
        launchOnFxThread {
            name.text = course.name
            description.text = course.description
            certificate.text = course.certificateField
            recordBook.text = course.recordBookField
            price.text = course.price.toString()
            name.requestFocus()
        }
    }

    fun getUpdated(): Course {
        course?.let {
            it.name = name.text
            it.description = description.text
            it.certificateField = certificate.text
            it.recordBookField = recordBook.text
            it.price = price.text.toDouble()
        }
        return course!!
    }

    fun createNew(): Course {
        return Course(
            name.text,
            description.text,
            certificate.text,
            recordBook.text,
            price.text.toDouble(),
            0,
            Cache.user.companyId
        )
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }
}