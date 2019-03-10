package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    public VBox login_form;
    public TextField login_email;
    public PasswordField login_password;
    public static boolean isLogged_in = false;
    static LoginHandler loginHandler;
    public Button login;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        if(isLogged_in){
//            login_form.setVisible(false);
//            login_form.setManaged(false);
//            dr_name.setText(loginHandler.dr.name);
//        }else {
//            login.setDefaultButton(true);
//            login_form.setVisible(true);
//            login_form.setManaged(true);
//        }
    }

    public void onLoginClicked(ActionEvent e) throws IOException{

        loginHandler = new LoginHandler();
        if(loginHandler.login(login_email.getText(), login_password.getText())){
            isLogged_in = true;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
            DashboardController.degree_category = DashboardController.UNDER_GRAD;
            Stage current_stage = new Stage(); //(Stage) ((Node)e.getTarget()).getScene().getWindow();
            current_stage.setTitle("Dashboard - Under graduates");
            Scene scene = new Scene(loader.load());
            current_stage.setScene(scene);
            current_stage.setMaximized(true);
            current_stage.show();
            ((Stage) ((Node)e.getTarget()).getScene().getWindow()).close();
        }


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
