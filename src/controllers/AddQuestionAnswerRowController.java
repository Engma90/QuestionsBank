package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Answer;


import java.net.URL;
import java.util.ResourceBundle;

public class AddQuestionAnswerRowController implements Initializable {
    public Label label;
    public MyHtmlEditor txt_answer;
    public CheckBox checkbox_right_answer;
    public RadioButton radio_right_answer;
    public Button remove_answer;
    public AddQuestionController addQuestionController;
    final AddQuestionAnswerRowController thisReference = this;
    FXMLLoader loader;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        checkbox_right_answer.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                addQuestionController.onRightAnswerChecked(thisReference, newValue);
//            }
//        });
//
//        radio_right_answer.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                addQuestionController.onRightAnswerChecked(thisReference, newValue);
//            }
//        });
    }

    public void onRemoveClicked(ActionEvent e){
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            addQuestionController.removeAnswerRow(this);
        }
    }

    public void setSelected(boolean val){
        //if(checkbox_right_answer.isVisible()){
            checkbox_right_answer.setSelected(val);
        //}
        //else if (radio_right_answer.isVisible()){
            radio_right_answer.setSelected(val);
        //}
    }
    public boolean getSelected(){
        if(checkbox_right_answer.isVisible()){
           return checkbox_right_answer.isSelected();
        }
        else {
            return radio_right_answer.isSelected();
        }
    }

    public void setSingleMode(ToggleGroup toggleGroup){
//        toggleGroup.getToggles().clear();
        radio_right_answer.setVisible(true);
        radio_right_answer.setManaged(true);
        checkbox_right_answer.setVisible(false);
        checkbox_right_answer.setManaged(false);
        radio_right_answer.setToggleGroup(toggleGroup);

    }

    public void setMultiMode(){
        radio_right_answer.setVisible(false);
        radio_right_answer.setManaged(false);
        checkbox_right_answer.setVisible(true);
        checkbox_right_answer.setManaged(true);
    }
}
