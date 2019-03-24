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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Course;
import models.CoursesListHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CoursesTableController implements Initializable {
    public TableView<Course> courses_table_view;
    private ChaptersTableController chaptersTableController;
    public TableColumn col_course_name,col_course_code,col_course_level;
    public ComboBox<String> course_level_filter;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //coursesListHandler = new CoursesListHandler();

        col_course_name.prefWidthProperty().bind(courses_table_view.widthProperty().divide(100).multiply(55)); // w * 1/4
        col_course_code.prefWidthProperty().bind(courses_table_view.widthProperty().divide(100).multiply(15));
        col_course_level.prefWidthProperty().bind(courses_table_view.widthProperty().divide(100).multiply(30));

        courses_table_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Course>() {
            @Override
            public void changed(ObservableValue<? extends Course> observable, Course oldValue, Course newValue) {

                int current_selected_index = courses_table_view.getSelectionModel().getSelectedIndex();//.getItems().indexOf(newValue);
                if(current_selected_index >= 0) {
                    chaptersTableController.course = courses_table_view.getSelectionModel().getSelectedItem();
                    chaptersTableController.refresh(true);
                }
                else {
                    chaptersTableController.course = new Course();
                    chaptersTableController.refresh(false);
                }
            }
        });



        course_level_filter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                courses_table_view.getItems().clear();
                courses_table_view.setItems(CoursesListHandler.getInstance().getCoursesList(newValue));
            }
        });


        courses_table_view.setItems(CoursesListHandler.getInstance().getCoursesList(course_level_filter.getValue()));
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
                    courses_table_view.getSelectionModel().selectLast();
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
            AddCourseController addCourseController =new AddCourseController("Edit", courses_table_view.getSelectionModel().getSelectedItem());
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
        CoursesListHandler.getInstance().Delete(courses_table_view.getSelectionModel().getSelectedItem().id);
        int selection = courses_table_view.getSelectionModel().getSelectedIndex() - 1;
        courses_table_view.getItems().clear();
        courses_table_view.setItems(CoursesListHandler.getInstance().getCoursesList(course_level_filter.getValue()));
        if(selection<0)
            selection = 0;
        courses_table_view.getSelectionModel().select(selection);
    }



    public void refresh(){
        int selection = courses_table_view.getSelectionModel().getSelectedIndex();
        courses_table_view.getItems().clear();
        courses_table_view.setItems(CoursesListHandler.getInstance().getCoursesList(course_level_filter.getValue()));
        if(!(courses_table_view.getItems().size() == 0)){
            if(selection<0)
                selection = 0;
            courses_table_view.getSelectionModel().select(selection);
        }

    }

    public void setChildController(Object Controller) {
        this.chaptersTableController = (ChaptersTableController) Controller;
    }
}
