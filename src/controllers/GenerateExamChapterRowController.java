package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GenerateExamChapterRowController implements Initializable,IUpdatable {
    public CheckBox isSelected;
    public Label lbl_chapter_name;
    public String chapter_id;
    public VBox content;
    public List<GenerateExamTopicRowController> generateExamTopicRowControllerList;
    public List<Topic> topicList;


    String chapter_name;
    String chapter_number;
    List<String> diff;
    public IUpdatable parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initUI(Chapter chapter){
        this.chapter_name = chapter.name;
        //this.diff = diff;
        this.chapter_id = chapter.id;
        this.chapter_number = chapter.number;
        this.lbl_chapter_name.setText(chapter_name);
        lbl_chapter_name.setMaxWidth(200);
        lbl_chapter_name.setMinWidth(200);
        isSelected.setSelected(true);
        isSelected.setOnAction(e ->{
            if(((CheckBox)e.getSource()).isSelected()){
                for (GenerateExamTopicRowController t:generateExamTopicRowControllerList){
                    t.isSelected.setSelected(true);
                }

            }else {
                for (GenerateExamTopicRowController t:generateExamTopicRowControllerList){
                    t.isSelected.setSelected(false);
                }
            }
            parent.update();
        });

        generateExamTopicRowControllerList = new ArrayList<>();
        topicList = TopicListHandler.getInstance().getList(new Chapter(chapter_id,"",""));
        List<Question> l;
        for (Topic t : topicList) {
            l = QuestionsTableHandler.getInstance().getQuestionList(t);

            int max_diff = Integer.MIN_VALUE;
            for (Question question:l){
                if(Integer.parseInt(question.getQuestion_diff())>max_diff)
                    max_diff = Integer.parseInt(question.getQuestion_diff());
            }
            if(l.size() > 0)
                addRow(t, l.size(), max_diff);
        }

    }


    private void addRow(Topic topic, int max_question, int max_diff) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GenerateExamTopicRow.fxml"));
            Parent p = loader.load();

            generateExamTopicRowControllerList.add((loader.getController()));
            ((GenerateExamTopicRowController) loader.getController()).topic_number_of_questions.setMax(max_question);
            ((GenerateExamTopicRowController) loader.getController()).diff_max_level.setMax(max_diff);
            ((GenerateExamTopicRowController) loader.getController()).initUI(topic, this);
            content.getChildren().add((p));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }
    }

//    void refreshSelection(){
//        int count = 0;
//        for (GenerateExamTopicRowController t:generateExamTopicRowControllerList){
//            if(t.isSelected.isSelected())
//                count++;
//        }
//        if(count == 0)
//            isSelected.setSelected(false);
//        else
//            isSelected.setSelected(true);
//    }

    @Override
    public void update() {
        int count = 0;
        for (GenerateExamTopicRowController t:generateExamTopicRowControllerList){
            if(t.isSelected.isSelected())
                count++;
        }
        if(count == 0)
            isSelected.setSelected(false);
        else
            isSelected.setSelected(true);
        parent.update();
    }
}
