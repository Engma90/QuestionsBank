package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Chapter;
import models.TopicListHandler;
import models.Topic;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTopicController implements Initializable, IWindow {
    Topic model;
    String operation_type;
    public TextField topic_name;
    public Button add_topic, edit_topic;
    public Chapter chapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(this.operation_type.contains("Add")){
            edit_topic.setVisible(false);
            edit_topic.setManaged(false);
            add_topic.setDefaultButton(true);
        }else {
            add_topic.setVisible(false);
            add_topic.setManaged(false);
            topic_name.setText(model.name);
            edit_topic.setDefaultButton(true);
        }
    }

    public AddTopicController(String operation_type, Chapter chapter, Topic model){
        this.operation_type = operation_type;
        this.model = model;
        this.chapter = chapter;
    }

    public void onAddTopicClicked(ActionEvent e){
        //new Topic();
        model.name = topic_name.getText();
        int success = TopicListHandler.getInstance().Add(chapter, model);

        if(success == -1){
            new Alert(Alert.AlertType.ERROR,"Operation Failed").show();
        }
        else {
            close(e);
        }

    }
    public void onEditTopicClicked(ActionEvent e){
        model.name = topic_name.getText();
        boolean success = TopicListHandler.getInstance().Edit(model);

        if(!success){
            new Alert(Alert.AlertType.ERROR,"Operation Failed").show();
        }
        else {
            close(e);
        }
    }

    private void close(ActionEvent e){
        // get a handle to the stage
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle(this.operation_type+" Topic");
        return null;
    }
}
