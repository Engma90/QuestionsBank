package controllers;

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
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddQuestionController implements Initializable, IWindow {
    private List<AddQuestionAnswerRowController> answerRowControllers;
    public Button add_question, edit_question, add_answer;
    public MyHtmlEditor question_html_editor;
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
    private boolean isADDorEdeitClicked = false;
    private static boolean isPrevConfigSet = false;

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

        question_html_editor.setToggleModeEnabled(false);
        radio_ext_match.setDisable(true);



        txt_q_exp_time.setDefaultVal(2);
        txt_q_exp_time.setMin(1);
        txt_q_exp_time.setMax(Integer.MAX_VALUE);

        txt_q_weight.setDefaultVal(5);
        txt_q_weight.setMin(0);
        txt_q_weight.setMax(10);

        txt_q_diff.setDefaultVal(50);
        txt_q_diff.setMin(0);
        txt_q_diff.setMax(100);

        answerRowControllers = new ArrayList<>();
        final ToggleGroup type_group = new ToggleGroup();
        radio_mcq.setToggleGroup(type_group);
        radio_true_false.setToggleGroup(type_group);
        radio_ext_match.setToggleGroup(type_group);
        final ToggleGroup true_false_answer_group = new ToggleGroup();
        radio_answer_true.setToggleGroup(true_false_answer_group);
        radio_answer_false.setToggleGroup(true_false_answer_group);



        isPrevConfigSet = false;
        radio_mcq.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                mcq_answers_list_scroll_pane.setVisible(true);
                mcq_answers_list_scroll_pane.setManaged(true);
                true_false_answers_list_scroll_pane.setVisible(false);
                true_false_answers_list_scroll_pane.setManaged(false);
                if (answerRowControllers.size() == 0 && operation_type.equals("Add")) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            add_row();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (answerRowControllers.size() == 0 && isPrevConfigSet) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            add_row();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                mcq_answers_list_scroll_pane.setVisible(false);
                mcq_answers_list_scroll_pane.setManaged(false);
                true_false_answers_list_scroll_pane.setVisible(true);
                true_false_answers_list_scroll_pane.setManaged(true);
            }
        });
        radio_answer_true.setSelected(true);
        radio_mcq.setSelected(true);
        if (this.operation_type.contains("Add")) {
            edit_question.setVisible(false);
            edit_question.setManaged(false);
            add_question.setVisible(true);
            add_question.setManaged(true);
        } else {
            add_question.setVisible(false);
            add_question.setManaged(false);
            edit_question.setVisible(true);
            edit_question.setManaged(true);
            try {
                setPrevConfig();
                isPrevConfigSet = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setPrevConfig() throws IOException {
        question_html_editor.setHtmlText(model.getQuestion_text());
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
        updateModelData();
        if (validate()) {
            isADDorEdeitClicked = true;
            QuestionsTableHandler.getInstance().Add(topic, model);
            close(e);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }


    public void onEditClicked(ActionEvent e) {
        updateModelData();
        if (validate()) {
            isADDorEdeitClicked = true;
            QuestionsTableHandler.getInstance().Edit(model);
            close(e);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
        }
    }

    private Question updateModelData() {
        String Q = question_html_editor.getHtmlText();
        String diff = txt_q_diff.getText();
        String weight = txt_q_weight.getText();
        String exp_time = txt_q_exp_time.getText();
        model.setQuestion_text(Q);
        model.setQuestion_diff(diff);
        model.setQuestion_weight(weight);
        model.setExpected_time(exp_time);

        if (radio_mcq.isSelected()) {
            model.setQuestion_type("MCQ");
            List<Answer> answers = new ArrayList<>();
            for (AddQuestionAnswerRowController a : answerRowControllers) {
                Answer answer = new Answer();
                answer.answer_text = a.txt_answer.getHtmlText();
                answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                answers.add(answer);
            }
            model.setAnswers(answers);

        } else if (radio_true_false.isSelected()) {
            model.setQuestion_type("True/False");
            String A = "True";
            String B = "False";
            List<Answer> answers = new ArrayList<>();
            Answer answer = new Answer();
            answer.answer_text = "True";
            answer.is_right_answer = radio_answer_true.isSelected() ? 1 : 0;
            answers.add(answer);
            answer = new Answer();
            answer.answer_text = "False";
            answer.is_right_answer = radio_answer_false.isSelected() ? 1 : 0;
            answers.add(answer);
            model.setAnswers(answers);
        }
        return model;
    }

    public void onAddAnswerClicked(ActionEvent e) throws IOException {
        add_row();
    }

    private boolean validate() {
        boolean isThereRightAnswer = false;
        if (Jsoup.parse(model.getQuestion_text()).text().isEmpty())
            return false;
        for (Answer answer : model.getAnswers()) {
            if (Jsoup.parse(answer.answer_text).text().isEmpty())
                return false;
            if (answer.is_right_answer == 1)
                isThereRightAnswer = true;
        }
        if (!isThereRightAnswer)
            return false;
        return true;
    }

    private void close(ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    @Override
    public boolean isSaveOnCloseRequired() {
        return true;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return isADDorEdeitClicked;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        stage.setTitle(operation_type + " Question");
        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        return this;
    }


// Todo: add listeners to all node to invalidate save status ICachInvadidatable
// or implement IUpdatable here and call it in Html, Numberfield, radio, rows

//    public static ArrayList<Node> getAllNodes(Parent root) {
//        ArrayList<Node> nodes = new ArrayList<Node>();
//        //nodes.get(0).property
//        addAllDescendents(root, nodes);
//        return nodes;
//    }
//    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
//        for (Node node : parent.getChildrenUnmodifiable()) {
//            nodes.add(node);
//            if (node instanceof Parent)
//                addAllDescendents((Parent)node, nodes);
//        }
//    }


}


