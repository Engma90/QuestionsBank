package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;


import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Button back_to_home;

    public AnchorPane right_content;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane pnlOne = null;

        try {
            pnlOne = FXMLLoader.load(this.getClass().getResource("../views/Questions.fxml"));
            right_content.getChildren().setAll(pnlOne);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onHomeClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Home.fxml"));
        Stage current_stage = (Stage) back_to_home.getScene().getWindow();
        current_stage.setTitle("Home");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();
    }
    public void onGenerateExamClicked(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../views/GenerateExam.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(back_to_home.getScene().getWindow());
            stage.setTitle("Generate Exam");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
