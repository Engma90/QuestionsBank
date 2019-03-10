package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.CourseModel;
import models.CoursesListHandler;


import java.net.URL;
import java.util.ResourceBundle;


public class AddCourseController implements Initializable {

    public TextField course_name,course_code;
    public Button add_course, edit_course;
    private String operation_type;
    private CourseModel model;
    public AddCourseController(String operation_type, CourseModel model){
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
            edit_course.setDefaultButton(true);
        }

    }
    public void onAddCourseClicked(ActionEvent e){
        //CoursesListHandler coursesListHandler =new CoursesListHandler();
        boolean success = CoursesListHandler.getInstance().Add(course_code.getText(),course_name.getText(),
                DashboardController.current_selected_dr_id,DashboardController.degree_category);
        if(!success){
            new Alert(Alert.AlertType.ERROR,"Operation Failed").show();
        }
        else {
            close(e);
        }
    }

    public void onEditCourseClicked(ActionEvent e){
        //CoursesListHandler coursesListHandler =new CoursesListHandler();
        boolean success = CoursesListHandler.getInstance().Edit(course_code.getText(),course_name.getText(),
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

}
