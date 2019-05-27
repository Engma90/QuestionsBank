package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Course;
import models.ILO;
import models.ILOsTableHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class AddILOController implements Initializable, IWindow {
    ILO model;
    String operation_type;
    public TextField ilo_code, ilo_description;
    public Button add_ilo, edit_ilo;
    public Course course;
    private boolean isADDorEdeitClicked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.operation_type.contains("add")) {
            edit_ilo.setVisible(false);
            edit_ilo.setManaged(false);
            add_ilo.setDefaultButton(true);
        } else {
            add_ilo.setVisible(false);
            add_ilo.setManaged(false);
            ilo_code.setText(model.getCode());
            ilo_description.setText(model.getDescription());
            edit_ilo.setDefaultButton(true);
        }
    }

    public AddILOController(String operation_type, Course course, ILO model) {
        this.operation_type = operation_type;
        this.model = model;
        this.course = course;
    }

    public void onAddILOClicked(ActionEvent e) {
        if (validate()) {
            isADDorEdeitClicked = true;
            model.setCode(ilo_code.getText());
            model.setDescription(ilo_description.getText());
            int success = ILOsTableHandler.getInstance().Add(course, model);

            if (success == -1) {
                new Alert(Alert.AlertType.ERROR, "Operation Failed").show();
            } else {
                close(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    public void onEditILOClicked(ActionEvent e) {
        if (validate()) {
            isADDorEdeitClicked = true;
            model.setCode(ilo_code.getText());
            model.setDescription(ilo_description.getText());
            boolean success = ILOsTableHandler.getInstance().Edit(model);

            if (!success) {
                new Alert(Alert.AlertType.ERROR, "Operation Failed").show();
            } else {
                close(e);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    private boolean validate() {
        return !ilo_code.getText().isEmpty() && !ilo_description.getText().isEmpty();
    }

    private void close(ActionEvent e) {
        // get a handle to the stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    @Override
    public boolean isSaveOnCloseRequired() {
        return false;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return isADDorEdeitClicked;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle(this.operation_type + " ILO");
        stage.setMinHeight(300);
        stage.setMinWidth(400);
        stage.setMaxHeight(300);
        stage.setMaxWidth(400);
        return this;
    }
}
