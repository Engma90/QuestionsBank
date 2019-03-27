package controllers;

import com.sun.javafx.css.Style;
import com.sun.javafx.css.Stylesheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Answer;
import models.Question;
import models.QuestionsTableHandler;
import models.Topic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.setUserAgentStylesheet;

public class AddQuestionController implements Initializable, IWindow {
    private List<AddQuestionAnswerRowController> answerRowControllers;
    public Button add_question, edit_question, add_answer;
    public MyHtmlEditor html_editor;
    public RadioButton radio_mcq;
    public RadioButton radio_true_false;
    public RadioButton radio_ext_match;

    public RadioButton radio_answer_true;
    public RadioButton radio_answer_false;
    public VBox mcq_ui_group, true_false_ui_group, mcq_ui_answers_list, container;

    public ScrollPane mcq_answers_list_scroll_pane, true_false_answers_list_scroll_pane;
    //public TextField txt_answer_a, txt_answer_b, txt_answer_c, txt_answer_d;
    //public ComboBox<String> combo_q_weight,combo_q_diff;

    public NumberField txt_q_diff, txt_q_weight, txt_q_exp_time;

    private String operation_type;
    private Question model;
    private Topic topic;

    public AddQuestionController(String operation_type, Topic topic, Question model) {
        this.operation_type = operation_type;
        this.model = model;
        this.topic = topic;

    }

    private AddQuestionAnswerRowController add_row() throws IOException {
//        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddQuestionAnswerRow.fxml"));
        Parent root = loader.load();

        mcq_ui_answers_list.getChildren().add((root));
        answerRowControllers.add((loader.getController()));
        ((AddQuestionAnswerRowController) loader.getController()).label.setText((char) (65 + (answerRowControllers.size() - 1)) + "");
        ((AddQuestionAnswerRowController) loader.getController()).addQuestionController = this;
        ((AddQuestionAnswerRowController) loader.getController()).loader = loader;
        return ((AddQuestionAnswerRowController) loader.getController());
//        } catch (IOException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
//            alert.show();
//            return null;
//        }
    }

    public void remove_row(AddQuestionAnswerRowController ans) {
        answerRowControllers.remove(ans);
        mcq_ui_answers_list.getChildren().remove((Node) ans.loader.getRoot());
        int count = 0;
        for (AddQuestionAnswerRowController a : answerRowControllers) {
            a.label.setText(((char) (65 + count) + ""));
            count++;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        html_editor.setToggleModeEnabled(false);
        radio_ext_match.setDisable(true);
        answerRowControllers = new ArrayList<>();

        txt_q_exp_time.setMin(0);
        txt_q_exp_time.setMax(Integer.MAX_VALUE);
        txt_q_diff.setMin(0);
        txt_q_diff.setMax(100);
        txt_q_weight.setMin(0);
        txt_q_weight.setMax(10);


//        combo_q_weight.setVisible(false);
//        combo_q_weight.setManaged(false);
        final ToggleGroup type_group = new ToggleGroup();
        radio_mcq.setToggleGroup(type_group);
        radio_true_false.setToggleGroup(type_group);
        radio_ext_match.setToggleGroup(type_group);
        final ToggleGroup answer_group = new ToggleGroup();
        //radio_answer_a.setToggleGroup(answer_group);
        //radio_answer_b.setToggleGroup(answer_group);
        //radio_answer_c.setToggleGroup(answer_group);
        //radio_answer_d.setToggleGroup(answer_group);

        final ToggleGroup true_false_answer_group = new ToggleGroup();
        radio_answer_true.setToggleGroup(true_false_answer_group);
        radio_answer_false.setToggleGroup(true_false_answer_group);


        radio_mcq.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                mcq_answers_list_scroll_pane.setVisible(true);
                mcq_answers_list_scroll_pane.setManaged(true);
                true_false_answers_list_scroll_pane.setVisible(false);
                true_false_answers_list_scroll_pane.setManaged(false);

            } else {
                mcq_answers_list_scroll_pane.setVisible(false);
                mcq_answers_list_scroll_pane.setManaged(false);
                true_false_answers_list_scroll_pane.setVisible(true);
                true_false_answers_list_scroll_pane.setManaged(true);
            }
        });
        radio_mcq.setSelected(true);
        if (this.operation_type.contains("Add")) {
            for (int i = 0; i < 4; i++) {
                try {
                    add_row();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            edit_question.setVisible(false);
            edit_question.setManaged(false);
            add_question.setVisible(true);
            add_question.setManaged(true);
            //add_question.setDefaultButton(true);
        } else {
            add_question.setVisible(false);
            add_question.setManaged(false);
            edit_question.setVisible(true);
            edit_question.setManaged(true);
            //edit_question.setDefaultButton(true);
            try {
                setPrevConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPrevConfig() throws IOException {
        html_editor.setHtmlText(model.getQuestion_text());
        txt_q_diff.setText(model.getQuestion_diff());
        txt_q_weight.setText(model.getQuestion_weight());
        txt_q_exp_time.setText(model.getExpected_time());
        if (model.getQuestion_type().equals("True/False")) {
            radio_true_false.setSelected(true);
            switch (model.getAnswers().get(0).is_right_answer) {
                case 1:
                    radio_answer_true.setSelected(true);
                    break;
                case 0:
                    radio_answer_false.setSelected(true);
                    break;
            }
        } else if (model.getQuestion_type().equals("MCQ")) {
            radio_mcq.setSelected(true);
            for (Answer a : model.getAnswers()) {
                AddQuestionAnswerRowController addQuestionAnswerRowController = add_row();
                if (addQuestionAnswerRowController != null) {
                    addQuestionAnswerRowController.txt_answer.setHtmlText(a.answer_text);
                    if (a.is_right_answer == 1) {
                        addQuestionAnswerRowController.checkbox_right_answer.setSelected(true);
                    } else {
                        addQuestionAnswerRowController.checkbox_right_answer.setSelected(false);
                    }
                }
            }
        }

    }

    public void onAddClicked(ActionEvent e) {
        String Q = html_editor.getHtmlText();
        String diff = txt_q_diff.getText();
        String weight = txt_q_weight.getText();
        String exp_time = txt_q_exp_time.getText();
        //QuestionsTableHandler questionsTableHandler = new QuestionsTableHandler();

        if (radio_mcq.isSelected()) {
            List<Answer> answers = new ArrayList<>();
            for (AddQuestionAnswerRowController a : answerRowControllers) {
                Answer answer = new Answer();
                answer.answer_text = a.txt_answer.getHtmlText();
                answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                answers.add(answer);
            }
//            if (validate(Q, diff, weight,A, B, C, D)){

            System.out.println("valid");
            model.setQuestion_text(Q);
            model.setQuestion_diff(diff);
            model.setQuestion_weight(weight);
            model.setExpected_time(exp_time);
            model.setQuestion_type("MCQ");
            model.setAnswers(answers);
            //model.setRight_answer(right_answer);
            QuestionsTableHandler.getInstance().Add(topic, model);
            close(e);
//            }else {
//                new Alert(Alert.AlertType.ERROR,"Please fill all fields").show();
//            }

        } else if (radio_true_false.isSelected()) {
            String A = "True";
            String B = "False";
            if (validate(Q, diff, weight)) {
                //String[] answers = new String[]{A,B};
                List<Answer> answers = new ArrayList<>();
                Answer answer = new Answer();
                answer.answer_text = "True";
                answer.is_right_answer = radio_answer_true.isSelected() ? 1 : 0;
                answers.add(answer);
                answer = new Answer();
                answer.answer_text = "False";
                answer.is_right_answer = radio_answer_false.isSelected() ? 1 : 0;
                answers.add(answer);

                model.setQuestion_text(Q);
                model.setQuestion_diff(diff);
                model.setQuestion_weight(weight);
                model.setExpected_time(exp_time);
                model.setQuestion_type("True/False");
                model.setAnswers(answers);
                //model.setRight_answer(right_answer);
                QuestionsTableHandler.getInstance().Add(topic, model);
                close(e);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            }
        }


    }


    public void onEditClicked(ActionEvent e) {
        String Q = html_editor.getHtmlText();
        String diff = txt_q_diff.getText();
        String weight = txt_q_weight.getText();
        String exp_time = txt_q_exp_time.getText();
        //QuestionsTableHandler questionsTableHandler = new QuestionsTableHandler();

        if (radio_mcq.isSelected()) {
            List<Answer> answers = new ArrayList<>();
            for (AddQuestionAnswerRowController a : answerRowControllers) {
                Answer answer = new Answer();
                answer.answer_text = a.txt_answer.getHtmlText();
                answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                answers.add(answer);
            }
//            if (validate(Q, diff, weight,A, B, C, D)){

            System.out.println("valid");
            model.setQuestion_text(Q);
            model.setQuestion_diff(diff);
            model.setQuestion_weight(weight);
            model.setExpected_time(exp_time);
            model.setQuestion_type("MCQ");
            model.setAnswers(answers);
            //model.setRight_answer(right_answer);
            QuestionsTableHandler.getInstance().Edit(model);
            close(e);
//            }else {
//                new Alert(Alert.AlertType.ERROR,"Please fill all fields").show();
//            }

        } else if (radio_true_false.isSelected()) {
            String A = "True";
            String B = "False";
            if (validate(Q, diff, weight)) {
                //String[] answers = new String[]{A,B};
                List<Answer> answers = new ArrayList<>();
                Answer answer = new Answer();
                answer.answer_text = "True";
                answer.is_right_answer = radio_answer_true.isSelected() ? 1 : 0;
                answers.add(answer);
                answer = new Answer();
                answer.answer_text = "False";
                answer.is_right_answer = radio_answer_false.isSelected() ? 1 : 0;
                answers.add(answer);

                model.setQuestion_text(Q);
                model.setQuestion_diff(diff);
                model.setQuestion_weight(weight);
                model.setExpected_time(exp_time);
                model.setQuestion_type("True/False");
                model.setAnswers(answers);
                //model.setRight_answer(right_answer);
                QuestionsTableHandler.getInstance().Edit(model);
                close(e);
            } else {
                new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            }
        }


    }
//
//        try {
//            //Save_to_file(Q);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

//    }

    public void onAddAnswerClicked(ActionEvent e) throws IOException {
        add_row();
    }

    private boolean validate(String Q, String diff, String weight, String A, String B, String C, String D) {
        if (Q.isEmpty() || A.isEmpty() || B.isEmpty() || C.isEmpty() || D.isEmpty()) {
            return false;
        } else if (diff.equals("Difficulty")) {//|| weight.equals("Weight")
            return false;
        }
//        else if(!radio_answer_a.isSelected() && !radio_answer_b.isSelected()
//                && !radio_answer_c.isSelected() && !radio_answer_d.isSelected()){
//            return false;
//        }
        return true;
    }

    private boolean validate(String Q, String diff, String weight) {

        if (Q.isEmpty()) {
            return false;
        } else if (diff.equals("Difficulty")) {
            return false;
        } else if (!radio_answer_true.isSelected() && !radio_answer_false.isSelected()) {
            return false;
        }
        return true;
    }


    private void close(ActionEvent e) {
        // get a handle to the stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle(operation_type + " Question");
        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        return null;
    }
}


