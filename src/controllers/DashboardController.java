package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Doctor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, IWindow {

    private CoursesTableController coursesTableController;
    private ChaptersTableController chaptersTableController;
    private TopicsTableController topicsTableController;
    private QuestionsTableController questionsTableController;
    //public SplitPane splitPane;

    public VBox right_content, chapters, topics;
    public VBox courses;
    //public static String current_selected_dr_id;
    public static Doctor doctor;
    public Button generate_exam, export_csv;
    public Label doctor_name;

    public DashboardController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doctor_name.setText("Dr. " + doctor.getName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CoursesTable.fxml"));
            Parent questions_root = loader.load();
            coursesTableController = loader.getController();
            courses.getChildren().setAll(((VBox) questions_root).getChildren());
            coursesTableController.generate_exam = generate_exam;
            coursesTableController.export_csv = export_csv;

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChaptersTable.fxml"));
            Parent questions_root = loader.load();
            chaptersTableController = loader.getController();
            chapters.getChildren().setAll(((VBox) questions_root).getChildren());

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TopicsTable.fxml"));
            Parent questions_root = loader.load();
            topicsTableController = loader.getController();
            topics.getChildren().setAll(((VBox) questions_root).getChildren());

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/QuestionsTable.fxml"));
            Parent questions_root = loader.load();
            questionsTableController = loader.getController();
            right_content.getChildren().setAll(((VBox) questions_root).getChildren());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }

        coursesTableController.setChildController(chaptersTableController);
        chaptersTableController.setChildController(topicsTableController);
        topicsTableController.setChildController(questionsTableController);
        coursesTableController.refresh("Init");
    }


    public void onGenerateExamClicked(ActionEvent e) {
        new WindowLoader().load(e,"/views/GenerateExam.fxml",null,null,true,false,
                coursesTableController.courses_table_view.getSelectionModel().getSelectedItem());
    }

    public void onExportClicked(ActionEvent e) {
        if (null != coursesTableController.courses_table_view.getSelectionModel().getSelectedItem()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "Question Bank Files (*.qb)",
                    "*.qb"));
            File selectedFile = fileChooser.showSaveDialog(((Node) e.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                QBBackup qbBackup = new QBBackup();
                String path = selectedFile.getAbsolutePath();
                if(!path.endsWith(".qb"))
                    path += ".qb";
                qbBackup.qbExport(coursesTableController.courses_table_view.getSelectionModel().getSelectedItem(),
                        path);
            }
        }
    }

    public void onImportClicked(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Question Bank Files (*.qb)", "*.qb"));
        File selectedFile = fileChooser.showOpenDialog(((Node) e.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            QBBackup qbBackup = new QBBackup();
            qbBackup.qbImport(selectedFile.getAbsolutePath());
            coursesTableController.refresh("Add");
            coursesTableController.courses_table_view.getSelectionModel().selectLast();
        }
    }
    public void onLogoutClicked(MouseEvent e) {
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            new WindowLoader().load(e, "/views/Home.fxml", null, null, false, true, null);
        }
        }

    @Override
    public boolean isSaveOnCloseRequired() {
        return false;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return false;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle("Dashboard");
        stage.setMaximized(true);
        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        return this;
    }
}
