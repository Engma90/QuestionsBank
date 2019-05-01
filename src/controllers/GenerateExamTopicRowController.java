package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import models.QuestionsTableHandler;
import models.Topic;

import java.net.URL;
import java.util.ResourceBundle;

public class GenerateExamTopicRowController implements Initializable {
    public CheckBox isSelected;
    public Label lbl_topic_name;
//    public String topic_id;
//    String topic_name;
    public Topic topic;
    public NumberField diff_max_level, topic_number_of_questions;
    public Label lbl_diff_max_level, lbl_topic_number_of_questions;

    public IUpdatable parent;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initUI(Topic topic, IUpdatable parent){
//        this.topic_name = topic.name;
//        this.topic_id = topic.id;
        this.topic = topic;
        //this.topic.AllQuestionsList = QuestionsTableHandler.getInstance().getQuestionList(topic);
        this.parent = parent;
        this.lbl_topic_name.setText(topic.name);

        //lbl_topic_name.setMaxWidth(200);
        //lbl_topic_name.setMinWidth(200);
        isSelected.setSelected(true);
        isSelected.setOnAction(e ->{
            parent.update();
        });
    }

}
