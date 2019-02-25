package controllers;

import javafx.scene.control.TextField;
import models.CoursesListHandler;



public class AddCourseController {

    public TextField course_name,course_code;
    public void onAddCourseClicked(){
        CoursesListHandler coursesListHandler =new CoursesListHandler();
        boolean success = coursesListHandler.Add(course_code.getText(),course_name.getText(),
                DashboardController.current_selected_dr_id,DashboardController.degree_category);
    }
}
