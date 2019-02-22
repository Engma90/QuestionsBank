package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionsController   implements Initializable {
    public TableColumn col_question_text, col_question_type, col_question_diff;
    public TableView questions_table;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_question_text.prefWidthProperty().bind(questions_table.widthProperty().divide(10).multiply(8)); // w * 1/4
        col_question_type.prefWidthProperty().bind(questions_table.widthProperty().divide(10)); // w * 1/2
        col_question_diff.prefWidthProperty().bind(questions_table.widthProperty().divide(10)); // w * 1/4

    }
    public void onDeleteClicked(){
        if(questions_table.getItems().size()>0) {
            int x = questions_table.getSelectionModel().getFocusedIndex();
            questions_table.getItems().remove(x);
        }

    }
    public void onAddClicked(ActionEvent e){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/AddQuestion.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Add Question");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
