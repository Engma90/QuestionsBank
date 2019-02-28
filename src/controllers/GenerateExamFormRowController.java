package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.QuestionModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GenerateExamFormRowController implements Initializable {
    public CheckBox isSelected;
    public Label lbl_chapter_name;
    public String chapter_id;
    int number_of_easy_questions = 0, number_of_medium_questions = 0, number_of_hard_questions = 0;
    public List<QuestionModel>easy_list, medium_list, hard_list;

    public int getNumber_of_questions() {
        return Integer.parseInt(number_of_questions.getText());
    }

    public List<String> getDiff() {
        return diff;
    }

    public Label number_of_questions;
    public NumberField[] difficulties;
    public NumberField Easy, Medium, Hard;
    String chapter_name;
    List<String> diff;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initUI(String chapter_id, String chapter_name, List<String> diff){
        this.chapter_name = chapter_name;
        this.diff = diff;
        this.chapter_id = chapter_id;
//        this.diff = diff.stream()
//                .map(String::toLowerCase)
//                .collect(Collectors.toList());

        System.out.println("AAAA");
        this.lbl_chapter_name.setText(chapter_name);
        difficulties = new NumberField[3];
        Easy.setPromptText("Easy");
        Medium.setPromptText("Medium");
        Hard.setPromptText("Hard");
        difficulties[0] = Easy;
        difficulties[1] = Medium;
        difficulties[2] = Hard;

        difficulties[0].setDisable(true);
        difficulties[1].setDisable(true);
        difficulties[2].setDisable(true);
        for (int i =0; i< 3; i++){
            if(diff.contains(difficulties[i].getId())){
                difficulties[i].setDisable(false);
            }
        }

        lbl_chapter_name.setMaxWidth(200);
        lbl_chapter_name.setMinWidth(200);
        isSelected.setSelected(true);
        isSelected.setOnAction(e ->{
            if(((CheckBox)e.getSource()).isSelected()){
                number_of_questions.setDisable(false);

                for (int i =0; i< 3; i++){
                    if(diff.contains(difficulties[i].getId())){
                        difficulties[i].setDisable(false);
                    }
                }

            }else {
                number_of_questions.setDisable(true);

                Easy.setDisable(true);
                Medium.setDisable(true);
                Hard.setDisable(true);
            }
        });
    }

    public int[] getQuestionsDistribution(){
        int number_of_questions = getNumber_of_questions();
        return null;
    }

}
