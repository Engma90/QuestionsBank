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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Question;
import models.QuestionsTableHandler;
import models.Topic;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionsTableController implements Initializable {
    public TableColumn col_question_text, col_question_type, col_question_diff, col_question_weight;
    public TableView<Question> questions_table;

//    public static String current_selected_question_id;
//    public static int current_selected_question_index;
    //public static QuestionsTableHandler questionsTableHandler;
    public Topic topic;
//    ContextMenu contextMenu;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topic = new Topic();
        col_question_text.prefWidthProperty().bind(questions_table.widthProperty().divide(10).multiply(8)); // w * 1/4
        col_question_type.prefWidthProperty().bind(questions_table.widthProperty().divide(10));
        col_question_diff.prefWidthProperty().bind(questions_table.widthProperty().divide(10));
        //col_question_weight.prefWidthProperty().bind(questions_table.widthProperty().divide(10)); // w * 1/4
        //questionsTableHandler = new QuestionsTableHandler();
        //QuestionsTableHandler.getInstance();
        refresh();
//        MenuItem add = new MenuItem("Add");
//        add.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                onAddClicked(e);
//            }
//        });
//        contextMenu = new ContextMenu();
//        contextMenu.getItems().add(add);
//        questions_table.setContextMenu(contextMenu);

        questions_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Question>() {
            @Override
            public void changed(ObservableValue<? extends Question> observable, Question oldValue, Question newValue) {
                System.out.println("Question:");
                int current_selected_index = questions_table.getSelectionModel().getSelectedIndex();
                if(current_selected_index >= 0){
//                current_selected_question_id = questions_table.getSelectionModel().getSelectedItem().getId();
//
//                current_selected_question_index = current_selected_index;
//
//                System.out.println(current_selected_question_index);
                }
                else {
//                    current_selected_question_index = -1;
//                    current_selected_question_id = "-1";
                }
            }
        });

    }

    public void onAddClicked(ActionEvent e){
        AddQuestionController addQuestionController =new AddQuestionController("Add", topic, new Question());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                int count1 = questions_table.getItems().size();
                refresh();
                int count2 = questions_table.getItems().size();
                if(count1 != count2)
                    questions_table.getSelectionModel().selectLast();
            }
        };
        new WindowLoader().load(e,"/views/AddQuestion.fxml",addQuestionController,onClose,true,false,null);
//        Parent root;
//        try {
//            FXMLLoader loader = new
//                    FXMLLoader(getClass().getResource("/views/AddQuestion.fxml"));
//            AddQuestionController addQuestionController =new AddQuestionController("Add", topic, new Question());
//            loader.setController(addQuestionController);
//            root = loader.load();
//            Stage stage = new Stage();
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(((Node)e.getSource()).getScene().getWindow());

//            stage.setScene(new Scene(root));
//            stage.setTitle("Add Question");
//            stage.setMinHeight(700);
//            stage.setMinWidth(1000);
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                public void handle(WindowEvent we) {
//                    int count1 = questions_table.getItems().size();
//                    refresh();
//                    int count2 = questions_table.getItems().size();
//                    if(count1 != count2)
//                        questions_table.getSelectionModel().selectLast();
//                }
//            });
//            stage.show();
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public void onEditClicked(ActionEvent e){
        AddQuestionController addQuestionController =new AddQuestionController("Edit", topic, questions_table.getSelectionModel().getSelectedItem());
        EventHandler<WindowEvent> onClose = new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                int selection = questions_table.getSelectionModel().getSelectedIndex();
                refresh();
                questions_table.getSelectionModel().select(selection);
            }
        };
        new WindowLoader().load(e,"/views/AddQuestion.fxml",addQuestionController,onClose,true,false,null);

//        Parent root;
//        try {
//            FXMLLoader loader = new
//                    FXMLLoader(getClass().getResource("/views/AddQuestion.fxml"));
//            AddQuestionController addQuestionController =new AddQuestionController("Edit", topic, questions_table.getSelectionModel().getSelectedItem());
//            loader.setController(addQuestionController);
//            root = loader.load();
//            Stage stage = new Stage();
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(((Node)e.getTarget()).getScene().getWindow());
//
//            stage.setScene(new Scene(root));
//
//            stage.setTitle("Edit Question");
//            stage.setMinHeight(700);
//            stage.setMinWidth(1000);
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                public void handle(WindowEvent we) {
//                    int selection = questions_table.getSelectionModel().getSelectedIndex();
//                    refresh();
//                    questions_table.getSelectionModel().select(selection);
//                }
//            });
//            stage.show();
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
    public void onDeleteQuestionClicked(ActionEvent e){
        Question model = questions_table.getSelectionModel().getSelectedItem();//new Question();
        //model.setId(current_selected_question_id);
        QuestionsTableHandler.getInstance().DeleteQuestion(model);
//        questions_table.getItems().clear();
//        questions_table.setItems(questionsTableHandler.getQuestionList());
        int selection = questions_table.getSelectionModel().getSelectedIndex() - 1;
        refresh();

        if(selection<0)
            selection = 0;
        questions_table.getSelectionModel().select(selection);
    }




    public void refresh(){
         //questionsTableHandler = new QuestionsTableHandler();
        questions_table.getItems().clear();
        ObservableList<Question> temp_list = QuestionsTableHandler.getInstance().getQuestionList(topic);
        if(temp_list.size() == 0){
//            current_selected_question_index = -1;
//            current_selected_question_id = "-1";
        }else {
            for (Question question : temp_list){
                System.out.println((Jsoup.parse(question.getQuestion_text()).text()));
                question.setRaw_text(((Jsoup.parse(question.getQuestion_text()).text())));
                System.out.println(question.getRaw_text());
            }
            questions_table.setItems(temp_list);
            questions_table.getSelectionModel().selectFirst();
        }


    }

}
