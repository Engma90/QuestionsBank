package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import models.QuestionModel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GenerateExamTopicRowController implements Initializable {
    public CheckBox isSelected;
    public Label lbl_topic_name;
    public String topic_id;
    String topic_name;
    public NumberField diff_max_level, topic_number_of_questions;
    public GenerateExamChapterRowController parent;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initUI(String topic_id, String topic_name, GenerateExamChapterRowController parent){
        this.topic_name = topic_name;
        this.topic_id = topic_id;
        this.parent = parent;
        this.lbl_topic_name.setText(topic_name);

        lbl_topic_name.setMaxWidth(200);
        lbl_topic_name.setMinWidth(200);
        isSelected.setSelected(true);
        isSelected.setOnAction(e ->{
            if(((CheckBox)e.getSource()).isSelected()){
                parent.refreshSelection();
            }else {
                parent.refreshSelection();
            }
        });
    }

}
