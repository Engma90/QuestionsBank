package controllers;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GenerateExamFormRowController implements Initializable {
    public CheckBox isSelected;
    public Label lbl_chapter_name;
    int number_of_easy_questions = 0, number_of_medium_questions = 0, number_of_hard_questions = 0;

    public int getNumber_of_questions() {
        return Integer.parseInt(number_of_questions.getText());
    }

    public List<String> getDiff() {
        return diff;
    }

    public TextField number_of_questions;
    public CheckBox[] difficulties;
    public CheckBox easy, medium, hard;
    String chapter_name;
    List<String> diff;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initUI(String chapter_name, List<String> diff){
        this.chapter_name = chapter_name;
        this.diff = diff;

        System.out.println("AAAA");
        this.lbl_chapter_name.setText(chapter_name);
        difficulties = new CheckBox[3];
        easy.setText("Easy");
        medium.setText("Medium");
        hard.setText("Hard");
        difficulties[0] = easy;
        difficulties[1] = medium;
        difficulties[2] = hard;

        difficulties[0].setDisable(true);
        difficulties[1].setDisable(true);
        difficulties[2].setDisable(true);
        for (int i =0; i< 3; i++){
            if(diff.contains(difficulties[i].getText())){
                difficulties[i].setDisable(false);
            }
        }

        lbl_chapter_name.setMaxWidth(300);
        lbl_chapter_name.setMinWidth(300);
        isSelected.setSelected(true);
        isSelected.setOnAction(e ->{
            if(((CheckBox)e.getSource()).isSelected()){
                number_of_questions.setDisable(false);

                for (int i =0; i< 3; i++){
                    if(diff.contains(difficulties[i].getText())){
                        difficulties[i].setDisable(false);
                    }
                }

            }else {
                number_of_questions.setDisable(true);

                easy.setDisable(true);
                medium.setDisable(true);
                hard.setDisable(true);
            }
        });
    }

    public int[] getQuestionsDistribution(){
        int number_of_questions = getNumber_of_questions();
        if(easy.isSelected()){
            number_of_easy_questions = number_of_questions / getDiff().size();
            if(medium.isSelected()){
                number_of_medium_questions = ((number_of_questions-number_of_easy_questions)/getDiff().size()-1);
                number_of_hard_questions = number_of_questions-(number_of_easy_questions+number_of_medium_questions);
            }
        }else if(medium.isSelected()){

        }else if(hard.isSelected()){

        }

        return null;
    }

//    public int[] getRowEquationData(){
//        try {
//           return new int[]{Integer.parseInt(number_of_questions.getText()), this.diff.size()};
//        }catch (Exception ex){
//            return null;
//        }
//    }

}
