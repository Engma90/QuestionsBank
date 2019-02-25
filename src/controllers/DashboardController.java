package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;


import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;
import models.ChapterModel;
import models.ChaptersListHandler;
import models.CourseModel;
import models.CoursesListHandler;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    static final String POST_GRAD = "post_grad";
    static final String UNDER_GRAD = "under_grad";

    public ListView<CourseModel> courses_list_view;
    public ListView<ChapterModel> chapters_list_view;
    private CoursesListHandler coursesListHandler;
    private ChaptersListHandler chaptersListHandler;


    public AnchorPane right_content;

    public static String degree_category;
    public static String current_selected_course_id;
    public static String current_selected_chapter_id;
    public static String current_selected_dr_id;

    DashboardController(String _degree_category,String dr_id){
        current_selected_dr_id = dr_id;
        degree_category = _degree_category;
        coursesListHandler = new CoursesListHandler();
        chaptersListHandler = new ChaptersListHandler();


    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(degree_category);
        AnchorPane pnlOne;
        try {
            pnlOne = FXMLLoader.load(this.getClass().getResource("/views/Questions.fxml"));
            right_content.getChildren().setAll(pnlOne.getChildren());

        } catch (IOException e) {
           Alert  alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        courses_list_view.setItems(coursesListHandler.getCoursesList());
        chapters_list_view.setItems(chaptersListHandler.getChaptersList());



        courses_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CourseModel>() {
            @Override
            public void changed(ObservableValue<? extends CourseModel> observable, CourseModel oldValue, CourseModel newValue) {
                System.out.println("Course:");
                int current_selected_index = courses_list_view.getItems().indexOf(newValue);
                current_selected_course_id = coursesListHandler.getCoursesList().get(current_selected_index).id;
                System.out.println(current_selected_course_id);
            }
        });

        chapters_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChapterModel>() {
            @Override
            public void changed(ObservableValue<? extends ChapterModel> observable, ChapterModel oldValue, ChapterModel newValue) {
                System.out.println("Chapter:");
                int current_selected_index = chapters_list_view.getItems().indexOf(newValue);
                current_selected_chapter_id = chaptersListHandler.getChaptersList().get(current_selected_index).id;
                System.out.println(current_selected_chapter_id);
            }
        });
    }
    @FXML
    public void onHomeClicked(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Home.fxml"));
        Stage current_stage = (Stage) ((Node)e.getTarget()).getScene().getWindow();
        current_stage.setTitle("Home");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();
    }

    @FXML
    public void onGenerateExamClicked(ActionEvent e){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/GenerateExam.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Generate Exam");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onAddCourseClicked(ActionEvent e){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/AddCourse.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Add Course");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onAddChapterClicked(ActionEvent e){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/AddChapter.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Add Chapter");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
