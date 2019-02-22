package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionsController   implements Initializable {
    public TableColumn question, question_type, question_diff;
    public TableView questions_table;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        question.prefWidthProperty().bind(questions_table.widthProperty().divide(10).multiply(8)); // w * 1/4
        question_type.prefWidthProperty().bind(questions_table.widthProperty().divide(10)); // w * 1/2
        question_diff.prefWidthProperty().bind(questions_table.widthProperty().divide(10)); // w * 1/4

    }
    public void onDeleteClicked(){
        TableRow selected_raw = new TableRow();
        if(questions_table.getItems().size()>0) {
            int x = questions_table.getSelectionModel().getFocusedIndex();
            questions_table.getItems().remove(x);
        }

    }
    public void onAddClicked(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/AddQuestion.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(questions_table.getScene().getWindow());
            stage.setTitle("Generate Exam");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
