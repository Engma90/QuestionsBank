package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.QuestionModel;
import models.QuestionTableHandler;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class QuestionsController   implements Initializable {
    public TableColumn col_question_text, col_question_type, col_question_diff, col_question_weight;
    public TableView<QuestionModel> questions_table;

    public static String current_selected_question_id;
    public static int current_selected_question_index;
    public static QuestionTableHandler questionTableHandler;
//    ContextMenu contextMenu;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_question_text.prefWidthProperty().bind(questions_table.widthProperty().divide(10).multiply(7)); // w * 1/4
        col_question_type.prefWidthProperty().bind(questions_table.widthProperty().divide(10));
        col_question_diff.prefWidthProperty().bind(questions_table.widthProperty().divide(10));
        col_question_weight.prefWidthProperty().bind(questions_table.widthProperty().divide(10)); // w * 1/4
        questionTableHandler = new QuestionTableHandler();
        refreshList();
//        MenuItem add = new MenuItem("Add");
//        add.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                onAddClicked(e);
//            }
//        });
//        contextMenu = new ContextMenu();
//        contextMenu.getItems().add(add);
//        questions_table.setContextMenu(contextMenu);

        questions_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QuestionModel>() {
            @Override
            public void changed(ObservableValue<? extends QuestionModel> observable, QuestionModel oldValue, QuestionModel newValue) {
                System.out.println("Question:");
                int current_selected_index = questions_table.getItems().indexOf(newValue);
                if(current_selected_index != -1)
                current_selected_question_id = questions_table.getItems().get(current_selected_index).getId();

                current_selected_question_index = current_selected_index;

                System.out.println(current_selected_question_index);
            }
        });

    }

    public void onAddClicked(ActionEvent e){
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddQuestion.fxml"));
            AddQuestionController addQuestionController =new AddQuestionController("Add", new QuestionModel());
            loader.setController(addQuestionController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getSource()).getScene().getWindow());
            stage.setTitle("Add Question");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
//                    questions_table.getItems().clear();
//                    ObservableList<QuestionModel> temp_list = questionTableHandler.getQuestionList();
////                    for (QuestionModel questionModel: temp_list){
////                        questionModel.raw_text.set(Jsoup.parse(questionModel.getQuestion_text()).text());
////                    }
//                    questions_table.setItems(temp_list);
                    refreshList();
                    questions_table.getSelectionModel().selectLast();
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void onEditClicked(ActionEvent e){
        Parent root;
        try {
            FXMLLoader loader = new
                    FXMLLoader(getClass().getResource("/views/AddQuestion.fxml"));
            AddQuestionController addQuestionController =new AddQuestionController("Edit", questions_table.getItems().get(current_selected_question_index));
            loader.setController(addQuestionController);
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
            stage.setTitle("Edit Question");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Closed");
//                    questions_table.getItems().clear();
//                    questions_table.setItems(questionTableHandler.getQuestionList());
                    refreshList();
                    questions_table.getSelectionModel().select(current_selected_question_index);
                }
            });
            stage.show();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onDeleteQuestionClicked(ActionEvent e){
        QuestionModel model = new QuestionModel();
        model.setId(current_selected_question_id);
        questionTableHandler.DeleteQuestion(model);
//        questions_table.getItems().clear();
//        questions_table.setItems(questionTableHandler.getQuestionList());
        refreshList();
        int selection = current_selected_question_index - 1;
        if(selection<0)
            selection = 0;
        questions_table.getSelectionModel().select(selection);
    }

    public void refreshList(){
         //questionTableHandler = new QuestionTableHandler();
        questions_table.getItems().clear();
        ObservableList<QuestionModel> temp_list = questionTableHandler.getQuestionList();
        for (QuestionModel questionModel: temp_list){
            System.out.println("1111111");
            System.out.println((Jsoup.parse(questionModel.getQuestion_text()).text()));
            System.out.println("2222222");
            questionModel.setRaw_text(((Jsoup.parse(questionModel.getQuestion_text()).text())));
            System.out.println("333333");
            System.out.println(questionModel.getRaw_text());
        }
        questions_table.setItems(temp_list);
        questions_table.getSelectionModel().selectFirst();
    }
}
