package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.WindowEvent;
import models.*;
import controllers.Vars.*;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionsTableController implements Initializable {
    public TableColumn col_question_text, col_question_type, col_question_diff, col_question_weight;
    public TableView<Question> questions_table;
    public Button btn_add, btn_edit, btn_delete;

    public Topic topic;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topic = new Topic();
        col_question_text.prefWidthProperty().bind(questions_table.widthProperty().divide(10).multiply(8)); // w * 1/4
        col_question_type.prefWidthProperty().bind(questions_table.widthProperty().divide(10));
        col_question_diff.prefWidthProperty().bind(questions_table.widthProperty().divide(10));


        questions_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Question>() {
            @Override
            public void changed(ObservableValue<? extends Question> observable, Question oldValue, Question newValue) {
                int current_selected_index = questions_table.getSelectionModel().getSelectedIndex();
                if (current_selected_index >= 0) {

                } else {
                }
            }
        });

        questions_table.setRowFactory(tv -> {
            TableRow<Question> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    onEditClicked(event);
                }
            });
            return row;
        });

    }

    public void onAddClicked(ActionEvent e) {
        AddQuestionController addQuestionController = new AddQuestionController(OperationType.ADD, topic, new Question());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                IWindow window = (IWindow) addQuestionController;
                if (window.isSaveOnCloseRequired() && !window.isSaveAndExitClicked()) {
                    if (Dialog.CreateDialog("Confirmation", "Do you want to close without Saving?",
                            "Yes", "No")) {
                    } else {
                        we.consume();
                    }
                }
                if (window.isSaveAndExitClicked()) {
                    refresh(true, OperationType.ADD);
                }
            }
        };
        new WindowLoader().load(e, "/views/AddQuestion.fxml", addQuestionController, onClose, true, false, null);
    }

    public void onEditClicked(Event e) {
        AddQuestionController addQuestionController = new AddQuestionController(OperationType.EDIT, topic, questions_table.getSelectionModel().getSelectedItem().clone());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                IWindow window = (IWindow) addQuestionController;
                if (window.isSaveOnCloseRequired() && !window.isSaveAndExitClicked()) {
                    if (Dialog.CreateDialog("Confirmation", "Do you want to close without Saving?",
                            "Yes", "No")) {
                    } else {
                        we.consume();
                    }
                }
                if (window.isSaveAndExitClicked()) { //window.isSaveAndExitClicked() //till fix bug (#4)
                    refresh(true, OperationType.EDIT);
                }
            }
        };
        new WindowLoader().load(e, "/views/AddQuestion.fxml", addQuestionController, onClose, true, false, null);
    }

    public void onDeleteQuestionClicked(ActionEvent e) {
        if (Dialog.CreateDialog("Confirmation", "Are you sure?", "Yes", "No")) {
            Question model = questions_table.getSelectionModel().getSelectedItem();
            QuestionsTableHandler.getInstance().Delete(model);
            refresh(true, OperationType.DELETE);
        }
    }



    void refresh(boolean parent_has_items, String Operation) {
        if (!parent_has_items) {
            questions_table.getItems().clear();
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
            btn_add.setDisable(true);
        } else {
            btn_add.setDisable(false);
            int selection = questions_table.getSelectionModel().getSelectedIndex();
            ObservableList<Question> tempList = QuestionsTableHandler.getInstance().getQuestionList(topic);
            questions_table.setItems(tempList);

            if (tempList.size() == 0) {
                btn_edit.setDisable(true);
                btn_delete.setDisable(true);
            } else {
                btn_edit.setDisable(false);
                btn_delete.setDisable(false);
                switch (Operation) {
                    case OperationType.INIT:
                        selection = 0;
                        break;

                    case OperationType.ADD:
                        selection = tempList.size() - 1;
                        break;
                    case OperationType.EDIT:
                        //No change
                        break;
                    case OperationType.DELETE:
                        selection--;
                        break;
                }
                if (selection < 0)
                    selection = 0;
                if (selection > tempList.size() - 1)
                    selection = tempList.size() - 1;

                int finalSelection = selection;
                Platform.runLater(() -> {
                    questions_table.getSelectionModel().select(finalSelection);
                });
            }
        }
    }

}
