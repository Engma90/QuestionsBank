package controllers;

import javafx.stage.Stage;

public interface IWindow{
    boolean isSaveOnCloseRequired();
    boolean isSaveAndExitClicked();
    Object setWindowData(Stage stage, Object initObject);
}
