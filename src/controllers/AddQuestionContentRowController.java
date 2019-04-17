package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import models.Answer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddQuestionContentRowController implements Initializable {
    public RadioButton select;
    public MyHtmlEditor content;
    public Button remove_content;
    public AddQuestionController addQuestionController;
    public HBox rowContainer;
    public List<Answer> rightAnswersList;
    FXMLLoader loader;
    final AddQuestionContentRowController thisReference = this;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rightAnswersList = new ArrayList<>();

        select.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println(newValue);
                addQuestionController.updateContentRowAnswers(thisReference, newValue);
            }
        });
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
