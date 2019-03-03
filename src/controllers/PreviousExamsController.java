package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.ExamModel;

import java.net.URL;
import java.util.ResourceBundle;

public class PreviousExamsController implements Initializable {
    public TableColumn col_name, col_date, col_year, col_model;
    public TableView<ExamModel> prev_exams_table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<ExamModel> examsList = FXCollections.observableArrayList();
        examsList.add(new ExamModel("1","2009","Name1","A","1st"));
        prev_exams_table.setItems(examsList);
    }
}
