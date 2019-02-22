package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void onUnderGradClicked(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
        DashboardController dbc = new DashboardController(DashboardController.UNDER_GRAD);
        loader.setController(dbc);
        Stage current_stage = (Stage) ((Node)e.getTarget()).getScene().getWindow();
        current_stage.setTitle("Dashboard-under graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();
    }
    public void onPostGradClicked(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
        DashboardController dbc = new DashboardController(DashboardController.POST_GRAD);
        loader.setController(dbc);
        Stage current_stage = (Stage) ((Node)e.getTarget()).getScene().getWindow();
        current_stage.setTitle("Dashboard-post graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();
    }
}
