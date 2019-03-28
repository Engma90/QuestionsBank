package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import models.Chapter;
import models.ChaptersListHandler;
import models.Course;

import java.net.URL;
import java.util.ResourceBundle;

public class ChaptersTableController implements Initializable {
    public ListView<Chapter> chapters_list_view;
    private TopicsTableController topicsTableController;
    public Course course;
    public Button btn_add,btn_edit, btn_delete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        course = new Course();
        chapters_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Chapter>() {
            @Override
            public void changed(ObservableValue<? extends Chapter> observable, Chapter oldValue, Chapter newValue) {

                int current_selected_index = chapters_list_view.getSelectionModel().getSelectedIndex();
                if (current_selected_index >= 0) {
                    topicsTableController.chapter = chapters_list_view.getSelectionModel().getSelectedItem();
                    topicsTableController.refresh(true, "Init");
                } else {
                    topicsTableController.chapter = new Chapter();
                    topicsTableController.refresh(false, "Init");
                }
            }
        });


        chapters_list_view.setCellFactory(new Callback<ListView<Chapter>, ListCell<Chapter>>() {
            @Override
            public ListCell<Chapter> call(ListView<Chapter> param) {
                ListCell<Chapter> listCell = new ListCell<Chapter>() {
                    @Override
                    protected void updateItem(Chapter item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                            setOnMouseClicked(null);
                        } else {
                            setText(item.toString());
                            setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    onEditChapterClicked(event);
                                }

                            });
                        }
                    }
                };
                return listCell;
            }
        });


        chapters_list_view.setItems(ChaptersListHandler.getInstance().getList(course));
    }


    public void onAddChapterClicked(ActionEvent e) {
        AddChapterController addChapterController = new AddChapterController("Add", course, new Chapter());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(((IWindow)addChapterController).isSaveAndExitClicked()){
                refresh(true, "Add");
                }
            }
        };
        new WindowLoader().load(e, "/views/AddChapter.fxml", addChapterController, onClose, true, false, null);
    }


    public void onEditChapterClicked(Event e) {
        AddChapterController addChapterController = new AddChapterController("Edit", course, chapters_list_view.getSelectionModel().getSelectedItem());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(((IWindow)addChapterController).isSaveAndExitClicked()){
                refresh(true,"Edit");
                }
            }
        };
        new WindowLoader().load(e, "/views/AddChapter.fxml", addChapterController, onClose, true, false, null);
    }

    public void onDeleteChapterClicked(ActionEvent e) {
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            Chapter model = chapters_list_view.getSelectionModel().getSelectedItem();
            ChaptersListHandler.getInstance().Delete(model);
            refresh(true, "Delete");
        }
    }


    void refresh(boolean parent_has_items, String Operation) {
        if (!parent_has_items) {
            chapters_list_view.getItems().clear();
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
            btn_add.setDisable(true);
            topicsTableController.refresh(false,"Init");
        } else {
            btn_add.setDisable(false);
            int selection = chapters_list_view.getSelectionModel().getSelectedIndex();
            ObservableList<Chapter> tempList = ChaptersListHandler.getInstance().getList(course);
            chapters_list_view.setItems(tempList);

            if(tempList.size() == 0){
                btn_edit.setDisable(true);
                btn_delete.setDisable(true);
                topicsTableController.refresh(false,"Init");
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
                    chapters_list_view.getSelectionModel().select(finalSelection);
                });
            }
        }
    }


    public void setChildController(Object Controller) {
        this.topicsTableController = (TopicsTableController) Controller;
    }


}
