package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void onUnderGradClicked(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
        DashboardController dbc = new DashboardController(DashboardController.UNDER_GRAD, "1");
        loader.setController(dbc);
        Stage current_stage = (Stage) ((Node)e.getTarget()).getScene().getWindow();
        current_stage.setTitle("Dashboard-under graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();
    }
    public void onPostGradClicked(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
        DashboardController dbc = new DashboardController(DashboardController.POST_GRAD, "1");
        loader.setController(dbc);
        Stage current_stage = (Stage) ((Node)e.getTarget()).getScene().getWindow();
        current_stage.setTitle("Dashboard-post graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);
        current_stage.show();
    }
    public void onSignupClicked(ActionEvent e){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/Signup.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Sign up");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
