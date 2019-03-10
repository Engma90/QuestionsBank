package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.ChapterModel;
import models.CoursesListHandler;
import models.TopicListHandler;
import models.TopicModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TopicsTableController implements Initializable {
    public ListView<TopicModel> topics_list_view;
//    static int current_selected_topic_index;
//    public static String current_selected_topic_id;
    private QuestionsTableController questionsTableController;
    public ChapterModel chapterModel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chapterModel = new ChapterModel();
        topics_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TopicModel>() {
            @Override
            public void changed(ObservableValue<? extends TopicModel> observable, TopicModel oldValue, TopicModel newValue) {

                int current_selected_index = topics_list_view.getSelectionModel().getSelectedIndex();
                if(current_selected_index >= 0){
//                    current_selected_topic_id = topics_list_view.getSelectionModel().getSelectedItem().id;
//                current_selected_topic_index = current_selected_index;

//                System.out.println("topic="+current_selected_topic_index);
                    questionsTableController.topicModel = topics_list_view.getSelectionModel().getSelectedItem();
                }
                else {
//                    current_selected_topic_id = "-1";
//                    current_selected_topic_index = -1;
                    questionsTableController.topicModel = new TopicModel();
                }
                questionsTableController.refresh();
            }
        });
    }




    public void onAddTopicClicked(ActionEvent e){
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddTopic.fxml"));
            AddTopicController addTopicController =new AddTopicController("Add", chapterModel, new TopicModel());
            loader.setController(addTopicController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Add Topic");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    try {
                        topics_list_view.getItems().clear();
                    }catch (Exception ex){

                    }

                    topics_list_view.setItems(TopicListHandler.getInstance().getTopicsList(chapterModel));
                    topics_list_view.getSelectionModel().selectLast();
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onEditTopicClicked(ActionEvent e){
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddTopic.fxml"));
            AddTopicController addTopicController =new AddTopicController("Edit", chapterModel, topics_list_view.getSelectionModel().getSelectedItem());
            loader.setController(addTopicController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Edit Topic");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    topics_list_view.getItems().clear();
                    topics_list_view.setItems(TopicListHandler.getInstance().getTopicsList(chapterModel));
                    topics_list_view.getSelectionModel().selectLast();
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onDeleteTopicClicked(ActionEvent e){
        TopicListHandler.getInstance().Delete(topics_list_view.getSelectionModel().getSelectedItem());
        topics_list_view.getItems().clear();
        topics_list_view.setItems(TopicListHandler.getInstance().getTopicsList(chapterModel));
        int selection = topics_list_view.getSelectionModel().getSelectedIndex() - 1;
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
        topics_list_view.setItems(TopicListHandler.getInstance().getTopicsList(chapterModel));
        if(topics_list_view.getItems().size() == 0){
//            current_selected_topic_id = "-1";
//            current_selected_topic_index = -1;
            //questionsTableController.refresh();
        }
        else {
            if(selection <0)
                selection = 0;
            topics_list_view.getSelectionModel().select(selection);
            //questionsTableController.refresh();
        }

    }
    public void setChildController(Object Controller) {
        this.questionsTableController = (QuestionsTableController) Controller;
    }

}
