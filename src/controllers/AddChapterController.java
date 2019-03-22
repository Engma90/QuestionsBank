package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Chapter;
import models.ChaptersListHandler;
import models.Course;

import java.net.URL;
import java.util.ResourceBundle;


public class AddChapterController implements Initializable {

    public TextField chapter_name,chapter_number;
    public Button add_chapter, edit_chapter;
    private String operation_type;
    private Chapter model;
    private Course course;
    public AddChapterController(String operation_type, Course course, Chapter model){
        this.operation_type = operation_type;
        this.model = model;
        this.course = course;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(this.operation_type.contains("Add")){
            edit_chapter.setVisible(false);
            edit_chapter.setManaged(false);
            add_chapter.setDefaultButton(true);
        }else {
            add_chapter.setVisible(false);
            add_chapter.setManaged(false);
            chapter_name.setText(model.name);
            chapter_number.setText(model.number);
            edit_chapter.setDefaultButton(true);
        }
    }
    public void onAddChapterClicked(ActionEvent e){
        //ChaptersListHandler chaptersListHandler =DashboardController.chaptersListHandler;
//        System.out.println("current_selected_course_id=" + DashboardController.current_selected_course_id);
//        System.out.println("chapter_name.getText()=" + chapter_name.getText());
        model.name = chapter_name.getText();
        model.number = chapter_number.getText();
        boolean success = ChaptersListHandler.getInstance().Add(course, model);
        if(!success){
            new Alert(Alert.AlertType.ERROR,"Operation Failed").show();
        }
        else {
            close(e);
        }
    }

    public void onEditChapterClicked(ActionEvent e){
        //ChaptersListHandler chaptersListHandler =new ChaptersListHandler();
        model.name = chapter_name.getText();
        model.number = chapter_number.getText();
        boolean success = ChaptersListHandler.getInstance().Edit(model);
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
