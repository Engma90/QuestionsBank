package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ILOsTableController implements Initializable, IWindow {
    public TableColumn col_code, col_description;
    public TableView<ILO> ilo_table;
    public Button btn_add, btn_edit, btn_delete;

    public Course course;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //course = new Course();
        col_code.prefWidthProperty().bind(ilo_table.widthProperty().divide(10).multiply(2));
        col_description.prefWidthProperty().bind(ilo_table.widthProperty().divide(10).multiply(8));


        ilo_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ILO>() {
            @Override
            public void changed(ObservableValue<? extends ILO> observable, ILO oldValue, ILO newValue) {
                int current_selected_index = ilo_table.getSelectionModel().getSelectedIndex();
                if (current_selected_index >= 0) {
                } else {
                }
            }
        });

        ilo_table.setRowFactory(tv -> {
            TableRow<ILO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    onEditClicked(event);
                }
            });
            return row;
        });
        refresh(true, Vars.OperationType.INIT);
    }

    public ILOsTableController(Course course){
        this.course = course;
    }

    public void onAddClicked(ActionEvent e) {
        AddILOController addILOController = new AddILOController(Vars.OperationType.ADD, course, new ILO());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                IWindow window = (IWindow) addILOController;
                if (window.isSaveOnCloseRequired() && !window.isSaveAndExitClicked()) {
                    if (Dialog.CreateDialog("Confirmation", "Do you want to close without Saving?",
                            "Yes", "No")) {
                    } else {
                        we.consume();
                    }
                }
                if (window.isSaveAndExitClicked()) {
                    refresh(true, Vars.OperationType.ADD);
                }
            }
        };
        new WindowLoader().load(e, "/views/AddILO.fxml", addILOController, onClose, true, false, null);
    }

    public void onEditClicked(Event e) {

        AddILOController addILOController = new AddILOController(Vars.OperationType.EDIT, course, ilo_table.getSelectionModel().getSelectedItem());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                IWindow window = (IWindow) addILOController;
                if (window.isSaveOnCloseRequired() && !window.isSaveAndExitClicked()) {
                    if (Dialog.CreateDialog("Confirmation", "Do you want to close without Saving?",
                            "Yes", "No")) {
                    } else {
                        we.consume();
                    }
                }
                if (window.isSaveAndExitClicked()) {
                    refresh(true, Vars.OperationType.EDIT);
                }
            }
        };
        new WindowLoader().load(e, "/views/AddILO.fxml", addILOController, onClose, true, false, null);
    }

    public void onDeleteClicked(ActionEvent e) {
        if (Dialog.CreateDialog("Confirmation", "Are you sure?", "Yes", "No")) {
            ILO model = ilo_table.getSelectionModel().getSelectedItem();
            ILOsTableHandler.getInstance().Delete(model.getId());
            refresh(true, Vars.OperationType.DELETE);
        }
    }



    void refresh(boolean parent_has_items, String Operation) {
        if (!parent_has_items) {
            ilo_table.getItems().clear();
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
            btn_add.setDisable(true);
        } else {
            btn_add.setDisable(false);
            int selection = ilo_table.getSelectionModel().getSelectedIndex();
            ObservableList<ILO> tempList = ILOsTableHandler.getInstance().getList(course);
            ilo_table.setItems(tempList);

            if (tempList.size() == 0) {
                btn_edit.setDisable(true);
                btn_delete.setDisable(true);
            } else {
                btn_edit.setDisable(false);
                btn_delete.setDisable(false);
                switch (Operation) {
                    case Vars.OperationType.INIT:
                        selection = 0;
                        break;

                    case Vars.OperationType.ADD:
                        selection = tempList.size() - 1;
                        break;
                    case Vars.OperationType.EDIT:
                        //No change
                        break;
                    case Vars.OperationType.DELETE:
                        selection--;
                        break;
                }
                if (selection < 0)
                    selection = 0;
                if (selection > tempList.size() - 1)
                    selection = tempList.size() - 1;

                int finalSelection = selection;
                Platform.runLater(() -> {
                    ilo_table.getSelectionModel().select(finalSelection);
                });
            }
        }
    }

    @Override
    public boolean isSaveOnCloseRequired() {
        return false;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return true;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {

        stage.setTitle("ILOs Editor");
        stage.setMinHeight(450);
        stage.setMinWidth(500);
//        stage.setMaxHeight(450);
//        stage.setMaxWidth(500);

        return this;
    }
}
