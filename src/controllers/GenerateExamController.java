package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GenerateExamController implements Initializable {
    public ComboBox number_of_models;
    public VBox same_or_different,shuffle;
    public RadioButton radio_same, radio_different, radio_shuffle_answers, radio_shuffle_questions, radio_both;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //number_of_models.getSelectionModel().select(0);
//        Stage current_stage = (Stage) number_of_models.getScene().getWindow();
//        current_stage.setTitle("Generate Exam");
        final ToggleGroup same_or_different_group = new ToggleGroup();
        final ToggleGroup shuffle_group = new ToggleGroup();
        radio_same.setToggleGroup(same_or_different_group);
        radio_different.setToggleGroup(same_or_different_group);
        radio_shuffle_answers.setToggleGroup(shuffle_group);
        radio_shuffle_questions.setToggleGroup(shuffle_group);
        radio_both.setToggleGroup(shuffle_group);

        shuffle.setVisible(false);
        shuffle.setManaged(false);
        same_or_different.setVisible(false);
        same_or_different.setManaged(false);

        number_of_models.setOnAction((e) -> {
            if(Integer.parseInt(number_of_models.getSelectionModel().getSelectedItem().toString()) > 1){
                same_or_different.setVisible(true);
                same_or_different.setManaged(true);
                shuffle.setVisible(true);
                shuffle.setManaged(true);
            }else {
                shuffle.setVisible(false);
                shuffle.setManaged(false);
                same_or_different.setVisible(false);
                same_or_different.setManaged(false);
            }

        });

        radio_same.setOnAction(e ->{
            if(radio_same.isSelected()){
                shuffle.setVisible(true);
                shuffle.setManaged(true);
                System.out.println("selected");
            }
        });
        radio_different.setOnAction(e ->{
            if(radio_different.isSelected()) {
                shuffle.setVisible(false);
                shuffle.setManaged(false);
                System.out.println("Not selected");
            }
        });

    }
}
