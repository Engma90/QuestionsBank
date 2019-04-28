package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Course;
import models.CoursesListHandler;


import java.net.URL;
import java.util.ResourceBundle;


public class AddCourseController implements Initializable, IWindow {

    public TextField course_name, course_code;
    public Button add_course, edit_course;
    private String operation_type;
    private Course model;
    public ComboBox<String> course_level,year;

    private boolean isADDorEdeitClicked = false;

    public AddCourseController(String operation_type, Course model) {
        this.operation_type = operation_type;
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.operation_type.contains("add")) {
            edit_course.setVisible(false);
            edit_course.setManaged(false);
            add_course.setVisible(true);
            add_course.setManaged(true);
            add_course.setDefaultButton(true);
        } else {
            add_course.setVisible(false);
            add_course.setManaged(false);
            edit_course.setVisible(true);
            edit_course.setManaged(true);
            course_name.setText(model.name);
            course_code.setText(model.code);
            course_level.setValue(model.level);
            year.setValue(model.year);
            edit_course.setDefaultButton(true);
        }

    }

    public void onAddCourseClicked(ActionEvent e) {
        if (validate()) {
            isADDorEdeitClicked = true;
            Course course = new Course();
            course.name = course_name.getText();
            course.code = course_code.getText();
            course.level = course_level.getValue();
            course.year = year.getValue();
            int success = CoursesListHandler.getInstance().Add(course);
            if (success == -1) {
                new Alert(Alert.AlertType.ERROR, "Operation Failed").show();
            } else {
                close(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    public void onEditCourseClicked(ActionEvent e) {
        if (validate()) {
            isADDorEdeitClicked = true;
            model.code=course_code.getText();
            model.name= course_name.getText();
            model.level = course_level.getValue();
            model.year = year.getValue();
            boolean success = CoursesListHandler.getInstance().Edit(model);
            if (!success) {
                new Alert(Alert.AlertType.ERROR, "Operation Failed").show();
            } else {
                close(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    private boolean validate() {
        return !course_code.getText().isEmpty() && !course_name.getText().isEmpty();
    }

    private void close(ActionEvent e) {
        // get a handle to the stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    @Override
    public boolean isSaveOnCloseRequired() {
        return false;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return isADDorEdeitClicked;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle(this.operation_type + " Course");
        stage.setMinHeight(400);
        stage.setMinWidth(500);
        stage.setMaxHeight(400);
        stage.setMaxWidth(500);

        return this;
    }
}
