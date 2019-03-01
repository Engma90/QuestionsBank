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
    public static final String POST_GRAD = "post_grad";
    public static final String UNDER_GRAD = "under_grad";

    public ListView<CourseModel> courses_list_view;
    public ListView<ChapterModel> chapters_list_view;
    public static CoursesListHandler coursesListHandler;
    public static  ChaptersListHandler chaptersListHandler;
    QuestionsController questionsController;

    public AnchorPane right_content;

    public static String degree_category;
    public static String current_selected_course_id;
    public static String current_selected_chapter_id;
    public static String current_selected_dr_id;
    static int current_selected_course_index;
    static int current_selected_chapter_index;



    public DashboardController(){

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coursesListHandler = new CoursesListHandler();
        chaptersListHandler = new ChaptersListHandler();
        System.out.println(degree_category);
        AnchorPane pnlOne;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Questions.fxml"));
            Parent questions_root = loader.load();
            questionsController = loader.getController();
            //pnlOne = FXMLLoader.load(this.getClass().getResource("/views/Questions.fxml"));
            right_content.getChildren().setAll(((AnchorPane)questions_root).getChildren());

        } catch (IOException e) {
           Alert  alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }


        courses_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CourseModel>() {
            @Override
            public void changed(ObservableValue<? extends CourseModel> observable, CourseModel oldValue, CourseModel newValue) {

                chapters_list_view.getItems().clear();
                current_selected_chapter_index = -1;
                current_selected_chapter_id = "-1";
                int current_selected_index = courses_list_view.getItems().indexOf(newValue);
                current_selected_course_id = courses_list_view.getItems().get(current_selected_index).id;
                System.out.println("get(current_selected_index).id "+current_selected_course_id);
                current_selected_course_index = current_selected_index;
                questionsController.refreshList();
                chapters_list_view.setItems(chaptersListHandler.getChaptersList());
                chapters_list_view.getSelectionModel().selectFirst();
                System.out.println("Course="+current_selected_course_index);


            }
        });

        chapters_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChapterModel>() {
            @Override
            public void changed(ObservableValue<? extends ChapterModel> observable, ChapterModel oldValue, ChapterModel newValue) {

                int current_selected_index = chapters_list_view.getItems().indexOf(newValue);
                if(current_selected_index != -1)
                current_selected_chapter_id = chapters_list_view.getItems().get(current_selected_index).id;
                current_selected_chapter_index = current_selected_index;
                questionsController.refreshList();
                System.out.println("Chapter="+current_selected_chapter_index);
            }
        });
//        courses_list_view.getSelectionModel().selectFirst();
//        chapters_list_view.getSelectionModel().selectFirst();


        courses_list_view.setItems(coursesListHandler.getCoursesList());
        courses_list_view.getSelectionModel().selectFirst();
        chapters_list_view.setItems(chaptersListHandler.getChaptersList());
        chapters_list_view.getSelectionModel().selectFirst();
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
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddCourse.fxml"));
            AddCourseController addCourseController =new AddCourseController("Add", null);
            loader.setController(addCourseController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Add Course");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    courses_list_view.getItems().clear();
                    courses_list_view.setItems(coursesListHandler.getCoursesList());
                    courses_list_view.getSelectionModel().selectLast();
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onEditCourseClicked(ActionEvent e){
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddCourse.fxml"));
            AddCourseController addCourseController =new AddCourseController("Edit", courses_list_view.getItems().get(current_selected_course_index));
            loader.setController(addCourseController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Edit Course");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    courses_list_view.getItems().clear();
                    courses_list_view.setItems(coursesListHandler.getCoursesList());
                    courses_list_view.getSelectionModel().select(current_selected_course_index);
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onDeleteCourseClicked(ActionEvent e){
        coursesListHandler.Delete(current_selected_course_id);
        courses_list_view.getItems().clear();
        courses_list_view.setItems(coursesListHandler.getCoursesList());
        int selection = current_selected_course_index - 1;
        if(selection<0)
            selection = 0;
        courses_list_view.getSelectionModel().select(selection);
    }


    public void onAddChapterClicked(ActionEvent e){
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddChapter.fxml"));
            AddChapterController addChapterController =new AddChapterController("Add", new ChapterModel());
            loader.setController(addChapterController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Add Chapter");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    chapters_list_view.getItems().clear();
                    chapters_list_view.setItems(chaptersListHandler.getChaptersList());
                    chapters_list_view.getSelectionModel().selectLast();
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void onEditChapterClicked(ActionEvent e){
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddChapter.fxml"));
            AddChapterController addChapterController =new AddChapterController("Edit",  chapters_list_view.getItems().get(current_selected_chapter_index));
            loader.setController(addChapterController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Edit Chapter");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                    chapters_list_view.getItems().clear();
                    chapters_list_view.setItems(chaptersListHandler.getChaptersList());
                    chapters_list_view.getSelectionModel().select(current_selected_chapter_index);
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onDeleteChapterClicked(ActionEvent e){
        ChapterModel model = new ChapterModel(current_selected_chapter_id, "");
        chaptersListHandler.Delete(model);
        chapters_list_view.getItems().clear();
        chapters_list_view.setItems(chaptersListHandler.getChaptersList());
        int selection = current_selected_chapter_index - 1;
        if(selection<0)
            selection = 0;
        chapters_list_view.getSelectionModel().select(selection);
    }


}
