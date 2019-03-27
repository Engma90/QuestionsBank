package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    public static String current_selected_dr_id;

    public DashboardController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        splitPane.setDividerPosition(0,0.50);
//        splitPane.setDividerPosition(1,0.25);
//        splitPane.setDividerPosition(0,0.25);
//        splitPane.setDividerPosition(1,0.50);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CoursesTable.fxml"));
            Parent questions_root = loader.load();
            coursesTableController = loader.getController();
            courses.getChildren().setAll(((VBox) questions_root).getChildren());

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
        coursesTableController.refresh();
        //splitPane.setDividerPositions(0.20f, 0.45f);
    }


    public void onGenerateExamClicked(ActionEvent e) {
        //Parent root;
        new WindowLoader().load(e,"/views/GenerateExam.fxml",null,null,true,false,
                coursesTableController.courses_table_view.getSelectionModel().getSelectedItem());
//        try {
//            //root = FXMLLoader.load(getClass().getResource("/views/GenerateExam.fxml"));
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GenerateExam.fxml"));
//            Parent root = loader.load();
//            GenerateExamController generateExamController = loader.getController();
////            generateExamController.course = coursesTableController.courses_table_view.getSelectionModel().getSelectedItem();
//            generateExamController.initUI();
//            Stage stage = new Stage();
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(((Node) e.getTarget()).getScene().getWindow());
//            stage.setTitle("Generate Exam");
//            stage.setMinHeight(700);
//            stage.setMinWidth(1000);
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
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
                qbBackup.qbExport(coursesTableController.courses_table_view.getSelectionModel().getSelectedItem(),
                        selectedFile.getAbsolutePath());
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
            coursesTableController.refresh();
            coursesTableController.courses_table_view.getSelectionModel().selectLast();
        }
    }
    public void onLogoutClicked(MouseEvent e) {
        new WindowLoader().load(e,"/views/Home.fxml",null,null,false,true, null);
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle("Dashboard");
        stage.setMaximized(true);
        return this;
    }
}
