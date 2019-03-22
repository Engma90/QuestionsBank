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
import models.Chapter;
import models.ChaptersListHandler;
import models.Course;

import java.io.IOException;
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
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddChapter.fxml"));
            AddChapterController addChapterController = new AddChapterController("Add", course, new Chapter());
            loader.setController(addChapterController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) e.getTarget()).getScene().getWindow());
            stage.setTitle("Add Chapter");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    chapters_list_view.getItems().clear();
                    chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(course));
                    chapters_list_view.getSelectionModel().selectLast();
                }
            });
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onEditChapterClicked(ActionEvent e) {
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddChapter.fxml"));
            AddChapterController addChapterController = new AddChapterController("Edit", course, chapters_list_view.getSelectionModel().getSelectedItem());
            loader.setController(addChapterController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) e.getTarget()).getScene().getWindow());
            stage.setTitle("Edit Chapter");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    int selection = chapters_list_view.getSelectionModel().getSelectedIndex();
                    chapters_list_view.getItems().clear();
                    chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(course));
                    chapters_list_view.getSelectionModel().select(selection);
                }
            });
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
