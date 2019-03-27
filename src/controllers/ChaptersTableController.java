package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;
import models.Chapter;
import models.ChaptersListHandler;
import models.Course;

import java.net.URL;
import java.util.ResourceBundle;

public class ChaptersTableController implements Initializable {
    public ListView<Chapter> chapters_list_view;
    private TopicsTableController topicsTableController;
    public Course course;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        course = new Course();
        chapters_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Chapter>() {
            @Override
            public void changed(ObservableValue<? extends Chapter> observable, Chapter oldValue, Chapter newValue) {

                int current_selected_index = chapters_list_view.getSelectionModel().getSelectedIndex();
                if (current_selected_index >= 0) {
                    topicsTableController.chapter = chapters_list_view.getSelectionModel().getSelectedItem();
                    topicsTableController.refresh(true);
                } else {
                    topicsTableController.chapter = new Chapter();
                    topicsTableController.refresh(false);
                }
            }
        });

        chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(course));
    }


    public void onAddChapterClicked(ActionEvent e) {
        AddChapterController addChapterController = new AddChapterController("Add", course, new Chapter());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                int count1 = chapters_list_view.getItems().size();
                refresh(true);
                int count2 = chapters_list_view.getItems().size();
                if(count1 != count2)
                    chapters_list_view.getSelectionModel().selectLast();
            }
        };
        new WindowLoader().load(e,"/views/AddChapter.fxml",addChapterController,onClose,true,false,null);
    }


    public void onEditChapterClicked(ActionEvent e) {
        AddChapterController addChapterController = new AddChapterController("Edit", course, chapters_list_view.getSelectionModel().getSelectedItem());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                int selection = chapters_list_view.getSelectionModel().getSelectedIndex();
                chapters_list_view.getItems().clear();
                chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(course));
                chapters_list_view.getSelectionModel().select(selection);
            }
        };
        new WindowLoader().load(e,"/views/AddChapter.fxml",addChapterController,onClose,true,false,null);
    }

    public void onDeleteChapterClicked(ActionEvent e) {
        Chapter model = chapters_list_view.getSelectionModel().getSelectedItem();
        ChaptersListHandler.getInstance().Delete(model);
        int selection = chapters_list_view.getSelectionModel().getSelectedIndex() - 1;
        chapters_list_view.getItems().clear();
        chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(course));
        if (selection < 0)
            selection = 0;
        chapters_list_view.getSelectionModel().select(selection);
    }


    public void refresh(boolean parent_has_items) {
        if (!parent_has_items) {
            chapters_list_view.getItems().clear();
            return;
        } else {
            int selection = chapters_list_view.getSelectionModel().getSelectedIndex();
            chapters_list_view.getItems().clear();
            chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(course));
            if (!(chapters_list_view.getItems().size() == 0)) {
                if (selection < 0)
                    selection = 0;
                chapters_list_view.getSelectionModel().select(selection);
            }
        }
    }


    public void setChildController(Object Controller) {
        this.topicsTableController = (TopicsTableController) Controller;
    }


}
