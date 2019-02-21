package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Button under_graduates;
    public Button post_graduates;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void onUnderGradClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Dashboard.fxml"));

        Stage current_stage = (Stage) under_graduates.getScene().getWindow();
        current_stage.setTitle("Dashboard-under graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();

    }
    public void onPostGradClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Dashboard.fxml"));

        Stage current_stage = (Stage) under_graduates.getScene().getWindow();
        current_stage.setTitle("Dashboard-post graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();

    }
}
