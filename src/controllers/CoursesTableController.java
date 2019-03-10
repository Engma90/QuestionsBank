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
import models.CourseModel;
import models.CoursesListHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CoursesTableController implements Initializable {
    public ListView<CourseModel> courses_list_view;
    private ChaptersTableController chaptersTableController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //coursesListHandler = new CoursesListHandler();

        courses_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CourseModel>() {
            @Override
            public void changed(ObservableValue<? extends CourseModel> observable, CourseModel oldValue, CourseModel newValue) {

                int current_selected_index = courses_list_view.getSelectionModel().getSelectedIndex();//.getItems().indexOf(newValue);
                if(current_selected_index >= 0) {
                    chaptersTableController.courseModel = courses_list_view.getSelectionModel().getSelectedItem();
                    chaptersTableController.refresh(true);
                }
                else {
                    chaptersTableController.courseModel = new CourseModel();
                    chaptersTableController.refresh(false);
                }
            }
        });
        courses_list_view.setItems(CoursesListHandler.getInstance().getCoursesList());
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
                    refresh();
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
            AddCourseController addCourseController =new AddCourseController("Edit", courses_list_view.getSelectionModel().getSelectedItem());
            loader.setController(addCourseController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Edit Course");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    refresh();
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onDeleteCourseClicked(ActionEvent e){
        CoursesListHandler.getInstance().Delete(courses_list_view.getSelectionModel().getSelectedItem().id);
        int selection = courses_list_view.getSelectionModel().getSelectedIndex() - 1;
        courses_list_view.getItems().clear();
        courses_list_view.setItems(CoursesListHandler.getInstance().getCoursesList());
        if(selection<0)
            selection = 0;
        courses_list_view.getSelectionModel().select(selection);
    }

    public void onGenerateExamClicked(ActionEvent e){
        //Parent root;
        try {
            //root = FXMLLoader.load(getClass().getResource("/views/GenerateExam.fxml"));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GenerateExam.fxml"));
            Parent root = loader.load();
            GenerateExamController generateExamController = loader.getController();
            generateExamController.courseModel = courses_list_view.getSelectionModel().getSelectedItem();
            generateExamController.initUI();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Generate Exam");
            stage.setMinHeight(700);
            stage.setMinWidth(1000);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void refresh(){
        int selection = courses_list_view.getSelectionModel().getSelectedIndex();
        courses_list_view.getItems().clear();
        courses_list_view.setItems(CoursesListHandler.getInstance().getCoursesList());
        if(!(courses_list_view.getItems().size() == 0)){
            if(selection<0)
                selection = 0;
            courses_list_view.getSelectionModel().select(selection);
        }

    }

    public void setChildController(Object Controller) {
        this.chaptersTableController = (ChaptersTableController) Controller;
    }
}
