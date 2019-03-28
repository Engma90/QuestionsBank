package controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import models.Chapter;
import models.Topic;
import models.TopicListHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class TopicsTableController implements Initializable {
    public ListView<Topic> topics_list_view;
    private QuestionsTableController questionsTableController;
    public Chapter chapter;
    public Button btn_add,btn_edit, btn_delete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chapter = new Chapter();
        topics_list_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            int current_selected_index = topics_list_view.getSelectionModel().getSelectedIndex();
            if (current_selected_index >= 0) {
                questionsTableController.topic = topics_list_view.getSelectionModel().getSelectedItem();
                questionsTableController.refresh(true, "Init");
            } else {
                questionsTableController.topic = new Topic();
                questionsTableController.refresh(false, "Init");
            }

        });

        topics_list_view.setCellFactory(new Callback<ListView<Topic>, ListCell<Topic>>() {
            @Override
            public ListCell<Topic> call(ListView<Topic> param) {
                ListCell<Topic> listCell = new ListCell<Topic>() {
                    @Override
                    protected void updateItem(Topic item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                            setOnMouseClicked(null);
                        } else {
                            setText(item.toString());
                            setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    onEditTopicClicked(event);
                                }

                            });
                        }
                    }
                };
                return listCell;
            }
        });


    }


    public void onAddTopicClicked(ActionEvent e) {
        AddTopicController addTopicController = new AddTopicController("Add", chapter, new Topic());
        EventHandler<WindowEvent> onClose = we -> {
            if(((IWindow)addTopicController).isSaveAndExitClicked()){
            refresh(true, "Add");
            }
        };
        new WindowLoader().load(e, "/views/AddTopic.fxml", addTopicController, onClose, true, false, null);

    }

    public void onEditTopicClicked(Event e) {
        AddTopicController addTopicController = new AddTopicController("Edit", chapter, topics_list_view.getSelectionModel().getSelectedItem());
        EventHandler<WindowEvent> onClose = we -> {
            if(((IWindow)addTopicController).isSaveAndExitClicked()){
            refresh(true, "Edit");
            }
        };
        new WindowLoader().load(e, "/views/AddTopic.fxml", addTopicController, onClose, true, false, null);

    }

    public void onDeleteTopicClicked(ActionEvent e) {
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            TopicListHandler.getInstance().Delete(topics_list_view.getSelectionModel().getSelectedItem());
            refresh(true, "Delete");
        }
    }


    void refresh(boolean parent_has_items, String Operation) {
        if (!parent_has_items) {
            topics_list_view.getItems().clear();
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
            btn_add.setDisable(true);
            questionsTableController.refresh(false,"Init");
        } else {
            btn_add.setDisable(false);
            int selection = topics_list_view.getSelectionModel().getSelectedIndex();
            ObservableList<Topic> tempList = TopicListHandler.getInstance().getList(chapter);
            topics_list_view.setItems(tempList);

            if(tempList.size() == 0){
                btn_edit.setDisable(true);
                btn_delete.setDisable(true);
                questionsTableController.refresh(false,"Init");
            }else {
                btn_edit.setDisable(false);
                btn_delete.setDisable(false);
                switch (Operation) {
                    case "Init":
                        selection = 0;
                        break;

                    case "Add":
                        selection = tempList.size()-1;
                        break;
                    case "Edit":
                        //No change
                        break;
                    case "Delete":
                        selection--;
                        break;
                }
                if(selection<0)
                    selection = 0;
                if(selection>tempList.size()-1)
                    selection = tempList.size()-1;

                int finalSelection = selection;
                Platform.runLater(()-> {
                    topics_list_view.getSelectionModel().select(finalSelection);
                });
            }
        }
    }

    void setChildController(Object Controller) {
        this.questionsTableController = (QuestionsTableController) Controller;
    }

}
