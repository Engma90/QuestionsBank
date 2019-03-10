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
import models.ChaptersListHandler;
import models.CourseModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChaptersTableController implements Initializable {
    public ListView<ChapterModel> chapters_list_view;
    private TopicsTableController topicsTableController;
    public CourseModel courseModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        courseModel = new CourseModel();
        chapters_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChapterModel>() {
            @Override
            public void changed(ObservableValue<? extends ChapterModel> observable, ChapterModel oldValue, ChapterModel newValue) {

                int current_selected_index = chapters_list_view.getSelectionModel().getSelectedIndex();
                if (current_selected_index >= 0) {
                    topicsTableController.chapterModel = chapters_list_view.getSelectionModel().getSelectedItem();
                    topicsTableController.refresh(true);
                } else {
                    topicsTableController.chapterModel = new ChapterModel();
                    topicsTableController.refresh(false);
                }
            }
        });

        chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(courseModel));
    }


    public void onAddChapterClicked(ActionEvent e) {
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddChapter.fxml"));
            AddChapterController addChapterController = new AddChapterController("Add", courseModel, new ChapterModel());
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
                    chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(courseModel));
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
            AddChapterController addChapterController = new AddChapterController("Edit", courseModel, chapters_list_view.getSelectionModel().getSelectedItem());
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
                    chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(courseModel));
                    chapters_list_view.getSelectionModel().select(selection);
                }
            });
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void onDeleteChapterClicked(ActionEvent e) {
        ChapterModel model = chapters_list_view.getSelectionModel().getSelectedItem();
        ChaptersListHandler.getInstance().Delete(model);
        int selection = chapters_list_view.getSelectionModel().getSelectedIndex() - 1;
        chapters_list_view.getItems().clear();
        chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(courseModel));
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
            chapters_list_view.setItems(ChaptersListHandler.getInstance().getChaptersList(courseModel));
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
