package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.SignupHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    public TextField full_name,email;
    public PasswordField password,re_password;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void onSignupClicked(ActionEvent e){
        if(validate()){
            SignupHandler signupHandler = new SignupHandler();
            signupHandler.Signup(full_name.getText(), email.getText(), password.getText());
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
}
