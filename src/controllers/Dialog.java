package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class Dialog {
    public static boolean CreateDialog(String title, String Message, String acceptBtn, String button2){
        ButtonType b1 = new ButtonType(acceptBtn, ButtonBar.ButtonData.OK_DONE);
        ButtonType b2 = new ButtonType(button2, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                Message,
                b1,
                b2);

        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(b2) == b1;
    }

    public static boolean CreateDialog(String title, String Message, String acceptBtn, String button2, Stage parent){
        ButtonType b1 = new ButtonType(acceptBtn, ButtonBar.ButtonData.OK_DONE);
        ButtonType b2 = new ButtonType(button2, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                Message,
                b1,
                b2);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(parent);

        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(b2) == b1;
    }
}
