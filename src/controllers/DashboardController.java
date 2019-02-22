package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;


import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    static final String POST_GRAD = "post_grad";
    static final String UNDER_GRAD = "under_grad";

    public AnchorPane right_content;
    private StringProperty operation_type = new SimpleStringProperty();
    DashboardController(String operation_type){
        this.operation_type.set(operation_type);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(this.operation_type.get());
        AnchorPane pnlOne;
        try {
            pnlOne = FXMLLoader.load(this.getClass().getResource("/views/Questions.fxml"));
            right_content.getChildren().setAll(pnlOne);
        } catch (IOException e) {
           Alert  alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }
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

}
