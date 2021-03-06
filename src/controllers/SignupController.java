package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.LoginHandler;
import models.SignupHandler;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Todo: - add fields translation, preferred exam layout language
         - Email already registered
         - Passwords match
 */
public class SignupController implements Initializable, IWindow {

    public TextField full_name,email,department;
    public PasswordField password,re_password;
    public MyButton signup;
    public ComboBox<String> combo_college;
    private SignupHandler signupHandler;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signupHandler = new SignupHandler();
        //signup.setDefaultButton(true);
        combo_college.setItems(signupHandler.getCollegesList());
        combo_college.getSelectionModel().selectFirst();

    }

    @Override
    public boolean isSaveOnCloseRequired() {
        return false;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return false;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setMinHeight(700);
        stage.setMinWidth(800);
        stage.setMaximized(true);
        stage.setTitle("Sign up");
        return this;
    }
    public void onSignupClicked(MouseEvent e){

        if(validate()){

            if(signupHandler.
                    Signup(full_name.getText(), email.getText(), password.getText(),
                            (combo_college.getSelectionModel().getSelectedIndex()+1)+"",
                            department.getText())){

                if(LoginHandler.getInstance().login(email.getText(), password.getText())){
                    new WindowLoader().load(e,"/views/Dashboard.fxml",null,null,false,true,null);
                }

                //close(e);
            }
            else {
                new Alert(Alert.AlertType.ERROR, "Operation failed").show();
            }

        }
        else {
            System.out.println("NotValid");
            new Alert(Alert.AlertType.ERROR, "Inputs are not valid").show();
        }
    }

    public void onBackClicked(MouseEvent e){
        new WindowLoader().load(e,"/views/Home.fxml",null,null,false,true,null);
    }


    //Todo: validate Preferred layout
    boolean validate(){
        if(!isValidEmail(email.getText())){
            return false;
        }
        if(full_name.getText().isEmpty() || email.getText().isEmpty()
        || password.getText().isEmpty() || re_password.getText().isEmpty()){
            return false;
        }else {
            return password.getText().equals(re_password.getText());
        }
    }

    private void close(MouseEvent e){
        // get a handle to the stage
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    public static boolean isValidEmail(String email)
    {
        if (email != null)
        {
            Pattern p = Pattern.compile("^([A-Za-z_01-9].*)([@])([A-Za-z_01-9].*).([a-z]+)$");
            Matcher m = p.matcher(email);
            return m.find();
        }
        return false;
    }


}
