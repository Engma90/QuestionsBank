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

    public TextField course_name,course_code;
    public Button add_course, edit_course;
    private String operation_type;
    private Course model;
    public ComboBox<String> course_level;
    public AddCourseController(String operation_type, Course model){
        this.operation_type = operation_type;
        this.model = model;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(this.operation_type.contains("Add")){
            edit_course.setVisible(false);
            edit_course.setManaged(false);
            add_course.setVisible(true);
            add_course.setManaged(true);
            add_course.setDefaultButton(true);
        }else {
            add_course.setVisible(false);
            add_course.setManaged(false);
            edit_course.setVisible(true);
            edit_course.setManaged(true);
            course_name.setText(model.name);
            course_code.setText(model.code);
            course_level.setValue(model.level);
            edit_course.setDefaultButton(true);
        }

    }
    public void onAddCourseClicked(ActionEvent e){
        //CoursesListHandler coursesListHandler =new CoursesListHandler();
        Course course = new Course();
        course.name = course_name.getText();
        course.code = course_code.getText();
        course.level = course_level.getValue();
        int success = CoursesListHandler.getInstance().Add(course);
        if(success == -1){
            new Alert(Alert.AlertType.ERROR,"Operation Failed").show();
        }
        else {
            close(e);
        }
    }

    public void onEditCourseClicked(ActionEvent e){
        //CoursesListHandler coursesListHandler =new CoursesListHandler();
        boolean success = CoursesListHandler.getInstance().Edit(course_code.getText(),course_name.getText(),course_level.getValue(),
                model.id);
        if(!success){
            new Alert(Alert.AlertType.ERROR,"Operation Failed").show();
        }
        else {
            close(e);
        }
    }

    private void close(ActionEvent e){
        // get a handle to the stage
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle(this.operation_type+" Course");

        return this;
    }
}
