package net.decodex.invoice.view

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import net.decodex.invoice.controller.CoursesDialogController
import net.decodex.invoice.domain.model.course.Course
import net.decodex.invoice.utils.FXLoader
import net.decodex.invoice.utils.LanguageUtils

class CourseDialog private constructor(private val course: Course?) : Dialog<Course>() {
    private var controller: CoursesDialogController

    init {
        val result = FXLoader.load<CoursesDialogController>("dialog_courses.fxml")
        controller = result.second
        setResultConverter(resultConverter())
        dialogPane.content = result.first
        dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL);
        if (course == null) {
            title = LanguageUtils.getString("add_item")
        } else {
            title = LanguageUtils.getString("edit_item")
            Platform.runLater { controller.fillTextFieldsWithData(course) }
        }
    }

    private fun resultConverter(): (param: ButtonType) -> Course? {
        return { dialogButton ->
            if (dialogButton == ButtonType.OK) {
                if (course == null) {
                    controller.createNew()
                } else {
                    controller.getUpdated()
                }
            } else {
                null
            }
        }
    }

    companion object {
        fun newInstance(course: Course? = null): CourseDialog {
            return CourseDialog(course)
        }
    }
}