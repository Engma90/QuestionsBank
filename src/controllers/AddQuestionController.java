package controllers;

import javafx.beans.property.BooleanProperty;
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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddQuestionController implements Initializable, IWindow {
    private List<AddQuestionAnswerRowController> answerRowControllers;
    private List<AddQuestionContentRowController> contentRowControllers;
    public Button add_question, edit_question, add_answer, add_question_content;
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
    private Question model, tempModel;

    private Topic topic;
    private boolean isADDorEdeitClicked = false;
    private boolean isPrevConfigSet = false;

    public AddQuestionController(String operation_type, Topic topic, Question model) {
        this.operation_type = operation_type;
        this.model = model;
        this.topic = topic;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txt_q_exp_time.setDefaultVal(2);
        txt_q_exp_time.setMin(1);
        txt_q_exp_time.setMax(Integer.MAX_VALUE);

        txt_q_weight.setDefaultVal(5);
        txt_q_weight.setMin(1);
        txt_q_weight.setMax(10);

        txt_q_diff.setDefaultVal(50);
        txt_q_diff.setMin(1);
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

//                if (answerRowControllers.size() == 0 && operation_type.equals(OperationType.ADD)) {
//                    for (int i = 0; i < 2; i++) {
//                        answerRowControllers.add(new Answer());
//                    }
//                } else if (answerRowControllers.size() == 0 && isPrevConfigSet) {
//                    for (int i = 0; i < 2; i++) {
//                        try {
//                            addAnswerRow();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }

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
            addQuestionContentRowController.content.setHtmlText(questionContent.getContent());

            //for (Answer a : questionContent.getAnswers()) {
            addQuestionContentRowController.rightAnswersList = questionContent.getAnswers();
            //}
        }
        System.out.println("Model:" + model.getContents().get(0).getAnswers().get(0).answer_text);
        System.out.println("contentRowControllers:" + contentRowControllers.get(0).rightAnswersList.get(0).answer_text);

        txt_q_diff.setText(model.getQuestion_diff());
        txt_q_weight.setText(model.getQuestion_weight());
        txt_q_exp_time.setText(model.getExpected_time());
        if (model.getQuestion_type().equals(QuestionType.TRUE_FALSE)) {
            radio_true_false.setSelected(true);
            switch (model.getContents().get(0).getAnswers().get(0).is_right_answer) {
                //Here add to list
                case 1:
                    radio_answer_true.setSelected(true);
                    break;
                case 0:
                    radio_answer_false.setSelected(true);
                    break;
            }
        } else if (model.getQuestion_type().equals(QuestionType.MCQ)) {
            radio_mcq.setSelected(true);
            System.out.println("Getting MCQ Answers");
            System.out.println(model.getContents().get(0).getAnswers().get(0));
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
            System.out.println("Getting EMQ Answers");
            System.out.println(model.getContents().get(0).getAnswers().get(0));
            for (Answer a : model.getContents().get(0).getAnswers()) {
                System.out.println(a.answer_text);
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

        System.out.println("Model2:" + model.getContents().get(0).getAnswers().get(0).answer_text);
        System.out.println("contentRowControllers2:" + contentRowControllers.get(0).rightAnswersList.get(0).answer_text);
    }

    public void onAddClicked(ActionEvent e) {

        for (AddQuestionContentRowController addQuestionContentRowController : contentRowControllers) {

            if ((addQuestionContentRowController.select.isSelected())) {
                // because data saved on un select set callUpdateModel false to avoid stackoverflow ex
                updateContentRowAnswers(addQuestionContentRowController, false);
                updateContentRowAnswers(addQuestionContentRowController, true);
            }
        }

        updateModelData();
        if (validate()) {
            this.model = tempModel;
            isADDorEdeitClicked = true;
            QuestionsTableHandler.getInstance().Add(topic, model);
            close(e);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields:\n" +
                    "Question\n" +
                    "At least 1 answer\n" +
                    "For each Question mark At least 1 answer as a right answer").show();
        }
    }


    public void onEditClicked(ActionEvent e) {
        for (AddQuestionContentRowController addQuestionContentRowController : contentRowControllers) {

            if ((addQuestionContentRowController.select.isSelected())) {
                // because data saved on un select set callUpdateModel false to avoid stackoverflow ex
                updateContentRowAnswers(addQuestionContentRowController, false);
                updateContentRowAnswers(addQuestionContentRowController, true);
            }
        }

        updateModelData();
        if (validate()) {
            this.model = tempModel;
            isADDorEdeitClicked = true;
            QuestionsTableHandler.getInstance().Edit(model);
            close(e);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields:\n" +
                    "Question\n" +
                    "At least 1 answer\n" +
                    "For each Question mark At least 1 answer as a right answer").show();
        }
    }

    private Question updateModelData() {
        tempModel = new Question();
        tempModel.setId(model.getId());
        for (AddQuestionContentRowController addQuestionContentRowController : contentRowControllers) {

            if (addQuestionContentRowController.rightAnswersList.size() == 0) {
                for (AddQuestionAnswerRowController a : answerRowControllers) {
                    Answer answer = new Answer();
                    answer.is_right_answer = 0;
                    addQuestionContentRowController.rightAnswersList.add(answer);
                }
            }

            //Todo: Solve only save on UnSelect
//            if ((addQuestionContentRowController.select.isSelected())){
//                // because data saved on un select set callUpdateModel false to avoid stackoverflow ex
//                updateContentRowAnswers(addQuestionContentRowController,false, false);
//                updateContentRowAnswers(addQuestionContentRowController,true, false);
//            }
        }

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


            String Q = addQuestionContentRowController.content.getHtmlText();
            String diff = txt_q_diff.getText();
            String weight = txt_q_weight.getText();
            String exp_time = txt_q_exp_time.getText();
            questionContent.setContent(Q);
            tempModel.setQuestion_diff(diff);
            tempModel.setQuestion_weight(weight);
            tempModel.setExpected_time(exp_time);

            if (radio_mcq.isSelected()) {
                tempModel.setQuestion_type(QuestionType.MCQ);
                List<Answer> answers = new ArrayList<>();
                for (AddQuestionAnswerRowController a : answerRowControllers) {
                    Answer answer = new Answer();
                    answer.answer_text = a.txt_answer.getHtmlText();
                    answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                    answers.add(answer);
                }
                questionContent.setAnswers(answers);

            } else if (radio_true_false.isSelected()) {
                tempModel.setQuestion_type(QuestionType.TRUE_FALSE);
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
                tempModel.setQuestion_type(QuestionType.EXTENDED_MATCH);
                List<Answer> answers = new ArrayList<>();
                int counter = 0;
                for (AddQuestionAnswerRowController a : answerRowControllers) {
                    Answer answer = new Answer();
                    answer.answer_text = a.txt_answer.getHtmlText();
                    //will set right answer in radio change event for each content opject
                    System.out.println("updateModelData: " + addQuestionContentRowController.rightAnswersList.size());


                    answer.is_right_answer = addQuestionContentRowController.rightAnswersList.get(counter++)
                            .is_right_answer == 1 ? 1 : 0;
                    //a.checkbox_right_answer.isSelected() ? 1 : 0;
                    answers.add(answer);
                }
                questionContent.setAnswers(answers);
            }
        }
        tempModel.setContents(questionContentList);
        //System.out.println(model.getContents().get(0).getAnswers().get(0));
        return tempModel;
    }

//    private void refreshContentRowAnswers() {
//        try {
//            for (AddQuestionContentRowController addQuestionContentRowController : contentRowControllers) {
//                if (addQuestionContentRowController.select.isSelected()) {
//                    addQuestionContentRowController.select.setSelected(false);
//                    addQuestionContentRowController.select.setSelected(true);
//                }
//            }
//        } catch (Exception ignored) {
//
//        }
//    }

    public void onAddAnswerClicked(ActionEvent e) throws IOException {
        for (AddQuestionContentRowController contentRowController : contentRowControllers) {
            Answer answer = new Answer();
            answer.answer_text = "";
            answer.id = "";
            answer.is_right_answer = 0;
            contentRowController.rightAnswersList.add(answer);
        }
        addAnswerRow();
    }

    public void onAddContentClicked(ActionEvent e) throws IOException {
        AddQuestionContentRowController contentRowController = addContentRow();

    }

    private AddQuestionContentRowController addContentRow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddQuestionContentRow.fxml"));
        Parent root = loader.load();

        question_contents_list.getChildren().add((root));
        contentRowControllers.add((loader.getController()));
        ((AddQuestionContentRowController) loader.getController()).addQuestionController = this;

        ((AddQuestionContentRowController) loader.getController()).select.setToggleGroup(questionContentGroup);
        ((AddQuestionContentRowController) loader.getController()).loader = loader;
        if (operation_type.equals(OperationType.ADD)) {
            for (int i = 0; i < 4; i++) {
                Answer answer = new Answer();
                answer.id = "";
                answer.answer_text = "";
                answer.is_right_answer = 0;
                ((AddQuestionContentRowController) loader.getController()).rightAnswersList.add(answer);
            }
        }
        return ((AddQuestionContentRowController) loader.getController());
    }


    public void updateContentRowAnswers(AddQuestionContentRowController contentRowController, Boolean newValue) {

        if (newValue) {
            System.out.println("updateContentRowAnswers_true");
            if (radio_true_false.isSelected()) {
            } else {
                if (contentRowController.rightAnswersList.size() != answerRowControllers.size()) {
                    contentRowController.rightAnswersList.clear();
                    for (AddQuestionAnswerRowController a : answerRowControllers) {
                        Answer answer = new Answer();
                        answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                        contentRowController.rightAnswersList.add(answer);
                    }
                } else {
                    for (int i = 0; i < answerRowControllers.size(); i++) {
                        if (contentRowController.rightAnswersList.get(i).is_right_answer == 1) {
                            answerRowControllers.get(i).checkbox_right_answer.setSelected(true);
                        } else {
                            answerRowControllers.get(i).checkbox_right_answer.setSelected(false);
                        }
                    }
                }
            }
        } else {
            System.out.println("updateContentRowAnswers_false");
            if (contentRowController.rightAnswersList.size() != answerRowControllers.size()) {
                contentRowController.rightAnswersList.clear();
                for (AddQuestionAnswerRowController a : answerRowControllers) {
                    Answer answer = new Answer();
                    answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                    contentRowController.rightAnswersList.add(answer);
                }
            } else {
                contentRowController.rightAnswersList.clear();
                for (AddQuestionAnswerRowController a : answerRowControllers) {
                    Answer answer = new Answer();
                    answer.is_right_answer = a.checkbox_right_answer.isSelected() ? 1 : 0;
                    contentRowController.rightAnswersList.add(answer);
                }

            }
            System.out.println("updateContentRowAnswers: " + contentRowController.rightAnswersList.size());
            //if(callUpdateModel)
            updateModelData();
        }
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
        //refreshContentRowAnswers();
        //if(operation_type.equals(OperationType.ADD))


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
        //refreshContentRowAnswers();

        for (AddQuestionContentRowController contentRowController : contentRowControllers) {
            contentRowController.rightAnswersList.remove(answerRowControllers.indexOf(ans));
        }

        answerRowControllers.remove(ans);
        mcq_ui_answers_list.getChildren().remove((Node) ans.loader.getRoot());
        int count = 0;
        for (AddQuestionAnswerRowController a : answerRowControllers) {
            a.label.setText(((char) (65 + count) + ""));
            count++;
        }
    }

    private boolean validate() {

        for (QuestionContent questionContent : tempModel.getContents()) {
            boolean isThereRightAnswer = false;
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
            if (!isThereRightAnswer) {
                System.out.println("No right Answer");
                return false;
            }
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


