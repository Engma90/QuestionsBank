package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AddQuestionContentRowController implements Initializable {
    public RadioButton select;
    public MyHtmlEditor txt_answer;
    public Button remove_content;
    public AddQuestionController addQuestionController;
    public HBox rowContainer;
    FXMLLoader loader;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onRemoveClicked(ActionEvent e){
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            addQuestionController.removeContentRow(this);
        }
    }

    public void setVisible(boolean val){
            rowContainer.setVisible(val);
    }
    public void setManaged(boolean val){
        rowContainer.setManaged(val);
    }

}
