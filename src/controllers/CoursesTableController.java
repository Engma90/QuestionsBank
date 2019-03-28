package controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import models.Course;
import models.CoursesListHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class CoursesTableController implements Initializable {
    public TableView<Course> courses_table_view;
    private ChaptersTableController chaptersTableController;
    public TableColumn col_course_name,col_course_code,col_course_level;
    public ComboBox<String> course_level_filter;
    public Button btn_edit,btn_delete;
    public Button export_csv, generate_exam;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //coursesListHandler = new CoursesListHandler();

        col_course_name.prefWidthProperty().bind(courses_table_view.widthProperty().divide(100).multiply(55)); // w * 1/4
        col_course_code.prefWidthProperty().bind(courses_table_view.widthProperty().divide(100).multiply(15));
        col_course_level.prefWidthProperty().bind(courses_table_view.widthProperty().divide(100).multiply(30));

        courses_table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            int current_selected_index = courses_table_view.getSelectionModel().getSelectedIndex();//.getItems().indexOf(newValue);
            if(current_selected_index >= 0) {
                chaptersTableController.course = courses_table_view.getSelectionModel().getSelectedItem();
                chaptersTableController.refresh(true,"Init");
            }
            else {
                chaptersTableController.course = new Course();
                chaptersTableController.refresh(false, "Init");
            }
        });

        courses_table_view.setRowFactory( tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    onEditCourseClicked(event);
                }
            });
            return row ;
        });


        course_level_filter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            courses_table_view.getItems().clear();
            courses_table_view.setItems(CoursesListHandler.getInstance().getList(newValue));
        });


        courses_table_view.setItems(CoursesListHandler.getInstance().getList(course_level_filter.getValue()));
    }

    public void onAddCourseClicked(ActionEvent e){
        //final int count1 = courses_table_view.getItems().size();
        final AddCourseController addCourseController =new AddCourseController("Add", null);

        EventHandler<WindowEvent> onClose = we -> {
            if(((IWindow)addCourseController).isSaveAndExitClicked()){
                refresh("Add");
            }

        };

        new WindowLoader().load(e,"/views/AddCourse.fxml",addCourseController,onClose,true,false,null);
    }
    public void onEditCourseClicked(Event e){
        AddCourseController addCourseController =new AddCourseController("Edit", courses_table_view.getSelectionModel().getSelectedItem());
        EventHandler<WindowEvent> onClose = we -> {
            if(((IWindow)addCourseController).isSaveAndExitClicked()){
            refresh("Edit");
            }
        };
        new WindowLoader().load(e,"/views/AddCourse.fxml",addCourseController,onClose,true,false,null);
    }
    public void onDeleteCourseClicked(ActionEvent e){
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            CoursesListHandler.getInstance().Delete(courses_table_view.getSelectionModel().getSelectedItem().id);
            refresh("Delete");
        }
    }



    void refresh(String Operation){
        int selection = courses_table_view.getSelectionModel().getSelectedIndex();
        ObservableList<Course> tempList = CoursesListHandler.getInstance().getList(course_level_filter.getValue());
        courses_table_view.setItems(tempList);

        if(tempList.size() == 0){
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
            export_csv.setDisable(true);
            generate_exam.setDisable(true);
            chaptersTableController.refresh(false,"Init");
        }else {
            export_csv.setDisable(false);
            generate_exam.setDisable(false);
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
                courses_table_view.getSelectionModel().select(finalSelection);
            });
        }
    }

    void setChildController(Object Controller) {
        this.chaptersTableController = (ChaptersTableController) Controller;
    }
}
