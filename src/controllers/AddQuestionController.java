package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.*;
import org.jsoup.Jsoup;
import controllers.Vars.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddQuestionController implements Initializable, IWindow {
    private List<AddQuestionAnswerRowController> answerRowControllers;
    private List<AddQuestionContentRowController> contentRowControllers;
    public Button add_question, edit_question, add_answer, add_question_content;
    //public MyHtmlEditor question_html_editor;
    public RadioButton radio_mcq;
    public RadioButton radio_true_false;
    public RadioButton radio_ext_match;

    final ToggleGroup questionContentGroup = new ToggleGroup();

    public RadioButton radio_answer_true;
    public RadioButton radio_answer_false;
    public VBox mcq_ui_group, true_false_ui_group, mcq_ui_answers_list, question_contents_list;

    public ScrollPane mcq_answers_list_scroll_pane, true_false_answers_list_scroll_pane;


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


    private AddQuestionContentRowController addContentRow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddQuestionContentRow.fxml"));
        Parent root = loader.load();

        question_contents_list.getChildren().add((root));
        contentRowControllers.add((loader.getController()));
        ((AddQuestionContentRowController) loader.getController()).addQuestionController = this;
        ((AddQuestionContentRowController) loader.getController()).select.setToggleGroup(questionContentGroup);
        ((AddQuestionContentRowController) loader.getController()).select.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //int count = 0;...
                ...
                //todo: on change update right answers
                // also update them on content model
                for (int j = 0; j < contentRowControllers.size(); j++) {
                    AddQuestionContentRowController contentRowController = contentRowControllers.get(j);
                    if (contentRowController.select.isSelected()) {
                        for (int i = 0; i < answerRowControllers.size(); i++) {
                            if(model.getContents().get(j).getAnswers().get(i).is_right_answer == 1){
                                answerRowControllers.get(i).checkbox_right_answer.setSelected(true);
                            }else {
                                answerRowControllers.get(i).checkbox_right_answer.setSelected(true);
                            }

                        }
                        break;
                    }
                }


                for (Answer a : model.getContents().get(0).getAnswers()) {
                    AddQuestionAnswerRowController addQuestionAnswerRowController = answerRowControllers;
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
        });
        ((AddQuestionContentRowController) loader.getController()).loader = loader;
        return ((AddQuestionContentRowController) loader.getController());
    }

    public void removeContentRow(AddQuestionContentRowController contentRowController) {
        if (contentRowControllers.size() > 1) {
            contentRowControllers.remove(contentRowController);
            question_contents_list.getChildren().remove((Node) contentRowController.loader.getRoot());
            int count = 0;
//            for (AddQuestionContentRowController a : contentRowControllers) {
//
//            }
            if (contentRowController.select.isSelected()) {
                contentRowControllers.get(0).select.setSelected(true);
            }
        }
    }


    private AddQuestionAnswerRowController addAnswerRow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddQuestionAnswerRow.fxml"));
        Parent root = loader.load();

        mcq_ui_answers_list.getChildren().add((root));
        answerRowControllers.add((loader.getController()));
        ((AddQuestionAnswerRowController) loader.getController()).label.setText((char) (65 + (answerRowControllers.size() - 1)) + "");
        ((AddQuestionAnswerRowController) loader.getController()).addQuestionController = this;
        ((AddQuestionAnswerRowController) loader.getController()).loader = loader;
        return ((AddQuestionAnswerRowController) loader.getController());
    }

    public void removeAnswerRow(AddQuestionAnswerRowController ans) {
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

        //question_html_editor.setToggleModeEnabled(false);
        //radio_ext_match.setDisable(true);


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
        contentRowControllers = new ArrayList<>();
        final ToggleGroup type_group = new ToggleGroup();
        radio_mcq.setToggleGroup(type_group);
        radio_true_false.setToggleGroup(type_group);
        radio_ext_match.setToggleGroup(type_group);


        final ToggleGroup true_false_answer_group = new ToggleGroup();
        radio_answer_true.setToggleGroup(true_false_answer_group);
        radio_answer_false.setToggleGroup(true_false_answer_group);


        isPrevConfigSet = false;


        radio_ext_match.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                for (AddQuestionContentRowController contentRowController : contentRowControllers) {
                    contentRowController.setVisible(true);
                    contentRowController.setManaged(true);
                }
                add_question_content.setVisible(true);
                add_question_content.setManaged(true);
                mcq_answers_list_scroll_pane.setVisible(true);
                mcq_answers_list_scroll_pane.setManaged(true);

                if (answerRowControllers.size() == 0 && operation_type.equals(OperationType.ADD)) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            addAnswerRow();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (answerRowControllers.size() == 0 && isPrevConfigSet) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            addAnswerRow();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


//                if (contentRowControllers.size() == 0 && operation_type.equals(OperationType.ADD)) {
//                    try {
//                        addContentRow();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } else if (contentRowControllers.size() == 0 && isPrevConfigSet) {
//                    try {
//                        addContentRow();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

            } else {
                add_question_content.setVisible(false);
                add_question_content.setManaged(false);
                mcq_answers_list_scroll_pane.setVisible(false);
                mcq_answers_list_scroll_pane.setManaged(false);
                for (AddQuestionContentRowController contentRowController : contentRowControllers) {
                    if (!contentRowController.select.isSelected()) {
                        contentRowController.setVisible(false);
                        contentRowController.setManaged(false);
                    }
                }
            }
        });

        radio_true_false.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                true_false_answers_list_scroll_pane.setVisible(true);
                true_false_answers_list_scroll_pane.setManaged(true);
            } else {
                true_false_answers_list_scroll_pane.setVisible(false);
                true_false_answers_list_scroll_pane.setManaged(false);
            }
        });

        radio_mcq.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                mcq_answers_list_scroll_pane.setVisible(true);
                mcq_answers_list_scroll_pane.setManaged(true);


                if (answerRowControllers.size() == 0 && operation_type.equals(OperationType.ADD)) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            addAnswerRow();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (answerRowControllers.size() == 0 && isPrevConfigSet) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            addAnswerRow();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                mcq_answers_list_scroll_pane.setVisible(false);
                mcq_answers_list_scroll_pane.setManaged(false);

            }
        });

//        //refresh_UI
//        radio_true_false.setSelected(true);
//        radio_ext_match.setSelected(true);
//        radio_mcq.setSelected(true);

        radio_answer_true.setSelected(true);// for user validation


        if (this.operation_type.contains(OperationType.ADD)) {
            edit_question.setVisible(false);
            edit_question.setManaged(false);
            add_question.setVisible(true);
            add_question.setManaged(true);
            radio_mcq.setSelected(true);

//            add_question_content.setVisible(false);
//            add_question_content.setManaged(false);
//
//            true_false_answers_list_scroll_pane.setVisible(false);
//            true_false_answers_list_scroll_pane.setManaged(false);

            try {
                addContentRow();
                contentRowControllers.get(0).select.setSelected(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
        //question_html_editor.setHtmlText(model.getQuestion_text());
        for (QuestionContent questionContent : model.getContents()) {
            AddQuestionContentRowController addQuestionContentRowController = addContentRow();
            addQuestionContentRowController.txt_answer.setHtmlText(questionContent.getContent());
        }

        txt_q_diff.setText(model.getQuestion_diff());
        txt_q_weight.setText(model.getQuestion_weight());
        txt_q_exp_time.setText(model.getExpected_time());
        if (model.getQuestion_type().equals(QuestionType.TRUE_FALSE)) {
            radio_true_false.setSelected(true);
            switch (model.getContents().get(0).getAnswers().get(0).is_right_answer) {
                case 1:
                    radio_answer_true.setSelected(true);
                    break;
                case 0:
                    radio_answer_false.setSelected(true);
                    break;
            }
        } else if (model.getQuestion_type().equals(QuestionType.MCQ)) {
            radio_mcq.setSelected(true);
            for (Answer a : model.getContents().get(0).getAnswers()) {
                AddQuestionAnswerRowController addQuestionAnswerRowController = addAnswerRow();
                if (addQuestionAnswerRowController != null) {
                    addQuestionAnswerRowController.txt_answer.setHtmlText(a.answer_text);
                    if (a.is_right_answer == 1) {
                        addQuestionAnswerRowController.checkbox_right_answer.setSelected(true);
                    } else {
                        addQuestionAnswerRowController.checkbox_right_answer.setSelected(false);
                    }
                }
            }
        } else if (model.getQuestion_type().equals(QuestionType.EXTENDED_MATCH)) {
            radio_ext_match.setSelected(true);
            for (Answer a : model.getContents().get(0).getAnswers()) {
                AddQuestionAnswerRowController addQuestionAnswerRowController = addAnswerRow();
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
        contentRowControllers.get(0).select.setSelected(true);
    }

    public void onAddClicked(ActionEvent e) {
        updateModelData();
        if (validate()) {
            isADDorEdeitClicked = true;
            QuestionsTableHandler.getInstance().Add(topic, model);
            close(e);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields:\n" +
                    "Question\n" +
                    "At least 1 answer\n" +
                    "Mark At least 1 answer as a right answer").show();
        }
    }


    public void onEditClicked(ActionEvent e) {
        updateModelData();
        if (validate()) {
            isADDorEdeitClicked = true;
            QuestionsTableHandler.getInstance().Edit(model);
            close(e);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields:\n" +
                    "Question\n" +
                    "At least 1 answer\n" +
                    "Mark At least 1 answer as a right answer").show();
        }
    }

    private Question updateModelData() {

        List<QuestionContent> questionContentList = new ArrayList<>();
        for (AddQuestionContentRowController addQuestionContentRowController : contentRowControllers) {
            if ((!addQuestionContentRowController.select.isSelected())
                    && (!radio_ext_match.isSelected())) {
                continue;
            }
            QuestionContent questionContent = new QuestionContent();

            if (!radio_ext_match.isSelected()) {
                // Add only if selected for non-extended types
                if (addQuestionContentRowController.select.isSelected()) {
                    questionContentList.add(questionContent);
                }
            } else {
                questionContentList.add(questionContent);
            }


            String Q = addQuestionContentRowController.txt_answer.getHtmlText();
            String diff = txt_q_diff.getText();
            String weight = txt_q_weight.getText();
            String exp_time = txt_q_exp_time.getText();
            questionContent.setContent(Q);
            model.setQuestion_diff(diff);
            model.setQuestion_weight(weight);
            model.setExpected_time(exp_time);

            if (radio_mcq.isSelected()) {
                model.setQuestion_type(QuestionType.MCQ);
                List<Answer> answers = new ArrayList<>();
                for (AddQuestionAnswerRowController a : answerRowControllers) {
                    Answer answer = new Answer();
                    answer.answer_text = a.txt_answer.getHtmlText();
                    answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                    answers.add(answer);
                }
                questionContent.setAnswers(answers);

            } else if (radio_true_false.isSelected()) {
                model.setQuestion_type(QuestionType.TRUE_FALSE);
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
                questionContent.setAnswers(answers);
            } else if (radio_ext_match.isSelected()) {
                model.setQuestion_type(QuestionType.EXTENDED_MATCH);
                List<Answer> answers = new ArrayList<>();
                for (AddQuestionAnswerRowController a : answerRowControllers) {
                    Answer answer = new Answer();
                    answer.answer_text = a.txt_answer.getHtmlText();
                    answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                    answers.add(answer);
                }
                questionContent.setAnswers(answers);
            }
        }
        model.setContents(questionContentList);
        return model;
    }

    public void onAddAnswerClicked(ActionEvent e) throws IOException {
        addAnswerRow();
    }

    public void onAddContentClicked(ActionEvent e) throws IOException {
        addContentRow();
    }

    private boolean validate() {
        boolean isThereRightAnswer = false;
        for (QuestionContent questionContent : model.getContents()) {
            if (Jsoup.parse(questionContent.getContent()).text().isEmpty() &&
                    Jsoup.parse(questionContent.getContent()).getElementsByTag("img").size() == 0)
                return false;
            for (Answer answer : questionContent.getAnswers()) {
                if (Jsoup.parse(answer.answer_text).text().isEmpty() &&
                        Jsoup.parse(answer.answer_text).getElementsByTag("img").size() == 0)
                    return false;
                if (answer.is_right_answer == 1)
                    isThereRightAnswer = true;
            }
            if (!isThereRightAnswer)
                return false;
        }

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
        stage.setWidth(stage.getMinWidth());
        stage.setHeight(stage.getMinHeight());
        return this;
    }


/* Todo: add listeners to all node to invalidate save status ICachInvadidatable
    or implement IUpdatable here and call it in Html, Numberfield, radio, rows */

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


