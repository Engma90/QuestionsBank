package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;
import models.Chapter;
import models.Topic;
import models.TopicListHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class TopicsTableController implements Initializable {
    public ListView<Topic> topics_list_view;
    private QuestionsTableController questionsTableController;
    public Chapter chapter;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chapter = new Chapter();
        topics_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Topic>() {
            @Override
            public void changed(ObservableValue<? extends Topic> observable, Topic oldValue, Topic newValue) {

                int current_selected_index = topics_list_view.getSelectionModel().getSelectedIndex();
                if(current_selected_index >= 0){
                    questionsTableController.topic = topics_list_view.getSelectionModel().getSelectedItem();
                }
                else {
                    questionsTableController.topic = new Topic();
                }
                questionsTableController.refresh();
            }
        });
    }




    public void onAddTopicClicked(ActionEvent e){
        AddTopicController addTopicController =new AddTopicController("Add", chapter, new Topic());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                int count1 = topics_list_view.getItems().size();
                refresh(true);
                int count2 = topics_list_view.getItems().size();
                if(count1 != count2)
                    topics_list_view.getSelectionModel().selectLast();
            }
        };
        new WindowLoader().load(e,"/views/AddTopic.fxml",addTopicController,onClose,true,false,null);

    }
    public void onEditTopicClicked(ActionEvent e){
        AddTopicController addTopicController =new AddTopicController("Edit", chapter, topics_list_view.getSelectionModel().getSelectedItem());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                refresh(true);
            }
        };
        new WindowLoader().load(e,"/views/AddTopic.fxml",addTopicController,onClose,true,false,null);

    }
    public void onDeleteTopicClicked(ActionEvent e){
        TopicListHandler.getInstance().Delete(topics_list_view.getSelectionModel().getSelectedItem());
        int selection = topics_list_view.getSelectionModel().getSelectedIndex() - 1;
        topics_list_view.getItems().clear();
        topics_list_view.setItems(TopicListHandler.getInstance().getTopicsList(chapter));

        if(selection<0)
            selection = 0;
        topics_list_view.getSelectionModel().select(selection);
    }


    public void refresh(boolean parent_has_items){
        if(!parent_has_items){
            topics_list_view.getItems().clear();
//            current_selected_topic_id = "-1";
//            current_selected_topic_index= -1;
            return;
        }
        int selection = topics_list_view.getSelectionModel().getSelectedIndex();
        topics_list_view.getItems().clear();
        topics_list_view.setItems(TopicListHandler.getInstance().getTopicsList(chapter));
        if(topics_list_view.getItems().size() == 0){
        }
        else {
            if(selection <0)
                selection = 0;
            topics_list_view.getSelectionModel().select(selection);
        }

    }
    public void setChildController(Object Controller) {
        this.questionsTableController = (QuestionsTableController) Controller;
    }

}
