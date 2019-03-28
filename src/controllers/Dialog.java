package controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

public class Dialog {
    public static boolean CreateDialog(String title, String Message, String button1, String button2){
        ButtonType b1 = new ButtonType(button1, ButtonBar.ButtonData.OK_DONE);
        ButtonType b2 = new ButtonType(button2, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                Message,
                b1,
                b2);

        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(b2) == b1;
    }
}
