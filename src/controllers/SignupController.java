package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.SignupHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    public TextField full_name,email;
    public PasswordField password,re_password;
    public Button signup;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signup.setDefaultButton(true);
    }
    public void onSignupClicked(ActionEvent e){
        if(validate()){
            SignupHandler signupHandler = new SignupHandler();
            signupHandler.Signup(full_name.getText(), email.getText(), password.getText());
            close(e);
        }
        else System.out.println("NotValid");
    }
    boolean validate(){
        if(full_name.getText().isEmpty() || email.getText().isEmpty()
        || password.getText().isEmpty() || re_password.getText().isEmpty()){
            return false;
        }else {
            return password.getText().equals(re_password.getText());
        }
    }

    private void close(ActionEvent e){
        // get a handle to the stage
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }
}
