package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.ChapterModel;
import models.ChaptersListHandler;
import models.CoursesListHandler;

import java.net.URL;
import java.util.ResourceBundle;


public class AddChapterController implements Initializable {

    public TextField chapter_name;
    public Button add_chapter, edit_chapter;
    private String operation_type;
    private ChapterModel model;
    public AddChapterController(String operation_type, ChapterModel model){
        this.operation_type = operation_type;
        this.model = model;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(this.operation_type.contains("Add")){
            edit_chapter.setVisible(false);
            edit_chapter.setManaged(false);
        }else {
            add_chapter.setVisible(false);
            add_chapter.setManaged(false);
            chapter_name.setText(model.name);
        }
    }
    public void onAddChapterClicked(ActionEvent e){
        //ChaptersListHandler chaptersListHandler =DashboardController.chaptersListHandler;
        System.out.println("current_selected_course_id=" + DashboardController.current_selected_course_id);
        System.out.println("chapter_name.getText()=" + chapter_name.getText());
        boolean success = DashboardController.chaptersListHandler.Add(chapter_name.getText(),DashboardController.current_selected_course_id);
        if(!success){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Failed to add");
        }
        close(e);
    }

    public void onEditChapterClicked(ActionEvent e){
        //ChaptersListHandler chaptersListHandler =new ChaptersListHandler();
        boolean success = DashboardController.chaptersListHandler.Edit(chapter_name.getText(),DashboardController.current_selected_chapter_id);
        if(!success){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Failed to edit");
        }
        close(e);
    }

    private void close(ActionEvent e){
        // get a handle to the stage
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }


}
