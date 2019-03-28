package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

public class AddQuestionAnswerRowController implements Initializable {
    public Label label;
    public MyHtmlEditor txt_answer;
    public CheckBox checkbox_right_answer;
    public Button remove_answer;
    public AddQuestionController addQuestionController;
    FXMLLoader loader;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onRemoveClicked(ActionEvent e){
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            addQuestionController.remove_row(this);
        }
    }
}
