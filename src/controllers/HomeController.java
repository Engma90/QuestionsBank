package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.LoginHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable, IWindow  {
    public AnchorPane main_node;
    public VBox login_form;
    public TextField login_email;
    public PasswordField login_password;
    public static boolean isLogged_in = false;
    static LoginHandler loginHandler;
    public MyButton login, signup;
    @Override
    public void initialize(URL location, ResourceBundle resources) {



        login_form.setVisible(false);
        login_form.setManaged(false);
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

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle("Home");
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setMaximized(true);
        return this;
    }
    public void onLoginClicked(MouseEvent e) throws IOException{

        loginHandler = new LoginHandler();
        if(loginHandler.login(login_email.getText(), login_password.getText())){
            isLogged_in = true;

//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
//            Stage current_stage = new Stage();
//
//            Scene scene = new Scene(loader.load());
//            current_stage.setScene(scene);
//            current_stage.show();
//            ((Stage) ((Node)e.getSource()).getScene().getWindow()).close();
            new WindowLoader().load(e,"/views/Dashboard.fxml",null,null,false,true,null);
        }


    }
    public void onSigninClicked(MouseEvent e) throws IOException{

        login_form.setVisible(true);
        login_form.setManaged(true);


    }
    public void onSignupClicked(MouseEvent e){
        new WindowLoader().load(e,"/views/Signup.fxml",null,null,true,false,null);
//        Parent root;
//        try {
//            root = FXMLLoader.load(getClass().getResource("/views/Signup.fxml"));
//            Stage stage = new Stage();
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
//            stage.setTitle("Sign up");
//            stage.setScene(new Scene(root));
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                public void handle(WindowEvent we) {
//                    System.out.println("Closed");
//                }
//            });
//            stage.show();
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }


}
