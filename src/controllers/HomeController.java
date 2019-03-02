package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.LoginHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public VBox login_form, navigate_form;
    public Label dr_name;
    public TextField login_email;
    public PasswordField login_password;
    public static boolean isLogged_in = false;
    static LoginHandler loginHandler;
    public Button login,under_graduates;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(isLogged_in){
            navigate_form.setVisible(true);
            navigate_form.setManaged(true);
            login_form.setVisible(false);
            login_form.setManaged(false);
            dr_name.setText(loginHandler.dr.name);


        }else {
            login.setDefaultButton(true);
            navigate_form.setVisible(false);
            navigate_form.setManaged(false);
            login_form.setVisible(true);
            login_form.setManaged(true);
        }
    }

    public void onLoginClicked(ActionEvent e){

        loginHandler = new LoginHandler();
        if(loginHandler.login(login_email.getText(), login_password.getText())){
            isLogged_in = true;
            navigate_form.setVisible(true);
            navigate_form.setManaged(true);
            login_form.setVisible(false);
            login_form.setManaged(false);
            dr_name.setText(loginHandler.dr.name);
            under_graduates.setDefaultButton(true);
        }


    }
    public void onUnderGradClicked(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
        DashboardController.degree_category = DashboardController.UNDER_GRAD;
        Stage current_stage = (Stage) ((Node)e.getTarget()).getScene().getWindow();
        current_stage.setTitle("Dashboard - Under graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);

        current_stage.setMaximized(false);
        current_stage.setMaximized(true);
        current_stage.show();
    }
    public void onPostGradClicked(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
        DashboardController.degree_category = DashboardController.POST_GRAD;
        Stage current_stage = (Stage) ((Node)e.getTarget()).getScene().getWindow();
        current_stage.setTitle("Dashboard - Post graduates");
        Scene scene = new Scene(loader.load());
        current_stage.setScene(scene);

        current_stage.setMaximized(false);
        current_stage.setMaximized(true);
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
