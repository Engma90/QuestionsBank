package controllers;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class WindowLoader {
    public  Object load(Event e, String fxml_path, Object custom_controller, EventHandler<WindowEvent> onCloseEvent, boolean init_modality, boolean close_parent, Object initObject){
        try {
            Stage stage = new Stage();
            Image icon = new Image(getClass().getResourceAsStream("/views/images/icon.png"));
            stage.getIcons().add(icon);
            Parent root;
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource(fxml_path));
            if(null != custom_controller){
                loader.setController(custom_controller);
            }
            root = loader.load();
            if(init_modality) {
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) e.getTarget()).getScene().getWindow());
            }
            stage.setScene(new Scene(root));
            ((IWindow) loader.getController()).setWindowData(stage, initObject);
            if(null != onCloseEvent){
                stage.setOnCloseRequest(onCloseEvent);
            }
            stage.show();
            if (close_parent) {
                ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
            }
            return stage;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
