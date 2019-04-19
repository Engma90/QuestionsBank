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


public class AddChapterController implements Initializable, IWindow {

    public TextField chapter_name;
    public NumberField chapter_number;
    public Button add_chapter, edit_chapter;
    private String operation_type;
    private Chapter model;
    private Course course;
    private boolean isADDorEdeitClicked = false;

    public AddChapterController(String operation_type, Course course, Chapter model) {
        this.operation_type = operation_type;
        this.model = model;
        this.course = course;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chapter_number.setDefaultVal(1);
        chapter_number.setMin(0);
        if (this.operation_type.contains("Add")) {
            edit_chapter.setVisible(false);
            edit_chapter.setManaged(false);
            add_chapter.setDefaultButton(true);
        } else {
            add_chapter.setVisible(false);
            add_chapter.setManaged(false);
            chapter_name.setText(model.name);
            chapter_number.setText(model.number);
            edit_chapter.setDefaultButton(true);
        }
    }

    public void onAddChapterClicked(ActionEvent e) {
        if (validate()) {
            isADDorEdeitClicked = true;
            model.name = chapter_name.getText();
            model.number = chapter_number.getText();
            int success = ChaptersListHandler.getInstance().Add(course, model);
            if (success == -1) {
                new Alert(Alert.AlertType.ERROR, "Operation Failed").show();
            } else {
                close(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    public void onEditChapterClicked(ActionEvent e) {
        if (validate()) {
            isADDorEdeitClicked = true;
            model.name = chapter_name.getText();
            model.number = chapter_number.getText();
            boolean success = ChaptersListHandler.getInstance().Edit(model);
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
        return !chapter_number.getText().isEmpty() && !chapter_name.getText().isEmpty();
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
        stage.setTitle(this.operation_type + " Chapter");
        stage.setMinHeight(300);
        stage.setMinWidth(400);
        stage.setMaxHeight(300);
        stage.setMaxWidth(400);
        return this;
    }

}
