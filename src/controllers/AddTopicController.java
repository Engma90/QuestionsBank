package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.ChapterModel;
import models.TopicListHandler;
import models.TopicModel;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTopicController implements Initializable {
    TopicModel model;
    String operation_type;
    public TextField topic_name;
    public Button add_topic, edit_topic;
    public ChapterModel chapterModel;

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

    public AddTopicController(String operation_type,ChapterModel chapterModel, TopicModel model){
        this.operation_type = operation_type;
        this.model = model;
        this.chapterModel = chapterModel;
    }

    public void onAddTopicClicked(ActionEvent e){
        //new TopicModel();
        model.name = topic_name.getText();
        boolean success = TopicListHandler.getInstance().Add(chapterModel, model);

        if(!success){
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
}
