package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public static final String POST_GRAD = "post_grad";
    public static final String UNDER_GRAD = "under_grad";

    CoursesTableController coursesTableController;
    ChaptersTableController chaptersTableController;
    TopicsTableController topicsTableController;
    QuestionsTableController questionsTableController;

    public AnchorPane right_content, courses, chapters, topics;
    public static String degree_category;
    public static String current_selected_dr_id;

    public DashboardController(){

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CoursesTable.fxml"));
            Parent questions_root = loader.load();
            coursesTableController = loader.getController();
            courses.getChildren().setAll(((AnchorPane)questions_root).getChildren());

        } catch (IOException e) {
            Alert  alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChaptersTable.fxml"));
            Parent questions_root = loader.load();
            chaptersTableController = loader.getController();
            chapters.getChildren().setAll(((AnchorPane)questions_root).getChildren());

        } catch (IOException e) {
            Alert  alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TopicsTable.fxml"));
            Parent questions_root = loader.load();
            topicsTableController = loader.getController();
            topics.getChildren().setAll(((AnchorPane)questions_root).getChildren());

        } catch (IOException e) {
            Alert  alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/QuestionsTable.fxml"));
            Parent questions_root = loader.load();
            questionsTableController = loader.getController();
            right_content.getChildren().setAll(((AnchorPane)questions_root).getChildren());
        } catch (IOException e) {
           Alert  alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        coursesTableController.setChildController(chaptersTableController);
        chaptersTableController.setChildController(topicsTableController);
        topicsTableController.setChildController(questionsTableController);
        coursesTableController.refresh();

    }
}
