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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddQuestionController implements Initializable, IWindow {
    public Button add_question, edit_question, add_answer, add_question_content;
    public RadioButton radio_mcq;
    public RadioButton radio_true_false;
    public RadioButton radio_ext_match;

    final ToggleGroup type_group = new ToggleGroup();
    final ToggleGroup questionContentToggleGroup = new ToggleGroup();
    final ToggleGroup questionContentAnswerToggleGroup = new ToggleGroup();

    public VBox ui_question_contents_list, ui_answers_list;
    public NumberField txt_q_diff, txt_q_weight, txt_q_exp_time;


    private List<AddQuestionAnswerRowController> answerRowControllers;
    final ContentHepler contentHepler = new ContentHepler();
    ;
    private String operation_type;
    private Question model;
    private Topic topic;
    private boolean isADDorEdeitClicked = false;
    private static final int DEFAULT_ANSWER_COUNT = 4;

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
        //contentHepler = new ContentHepler();

        radio_mcq.setToggleGroup(type_group);
        radio_true_false.setToggleGroup(type_group);
        radio_ext_match.setToggleGroup(type_group);

        //Todo: show warning before change.
        radio_mcq.selectedProperty().addListener(MCQListener);
        radio_true_false.selectedProperty().addListener(TrueFalseListener);
        radio_ext_match.selectedProperty().addListener(extendedMatchListener);

        if (this.operation_type.contains(OperationType.ADD)) {
            edit_question.setVisible(false);
            edit_question.setManaged(false);
            add_question.setVisible(true);
            add_question.setManaged(true);

            radio_mcq.setSelected(true);

            try {
                contentHepler.add(null);
                for (int i = 0; i < DEFAULT_ANSWER_COUNT; i++) {
                    try {
                        addAnswerRow(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contentHepler.selectItem(0);
    }

    private void setPrevConfig() throws IOException {
        txt_q_diff.setText(model.getQuestion_diff());
        txt_q_weight.setText(model.getQuestion_weight());
        txt_q_exp_time.setText(model.getExpected_time());

        for (Answer a : model.getAnswers()) {
            addAnswerRow(a);
        }

        for (QuestionContent questionContent : model.getContents()) {
            contentHepler.add(questionContent);
        }

        if (model.getQuestion_type().equals(QuestionType.MCQ))
            radio_mcq.setSelected(true);
        else if (model.getQuestion_type().equals(QuestionType.TRUE_FALSE))
            radio_true_false.setSelected(true);
        else if (model.getQuestion_type().equals(QuestionType.EXTENDED_MATCH))
            radio_ext_match.setSelected(true);
    }

    public void onAddClicked(ActionEvent e) {
        model.getContents().get(contentHepler.getCurrentSeletedIndex()).getRightAnswers().clear();
        for (int i = 0; i < answerRowControllers.size(); i++) {
            if (answerRowControllers.get(i).getSelected()) {
                model.getContents().get(contentHepler.getCurrentSeletedIndex())
                        .getRightAnswers().add(model.getAnswers().get(i));
            }
        }

        updateModelData();
        if (validate()) {
            //this.model = tempModel;
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
        model.getContents().get(contentHepler.getCurrentSeletedIndex()).getRightAnswers().clear();
        for (int i = 0; i < answerRowControllers.size(); i++) {

            if (answerRowControllers.get(i).getSelected()) {
                model.getContents().get(contentHepler.getCurrentSeletedIndex())
                        .getRightAnswers().add(model.getAnswers().get(i));
            }
        }

        updateModelData();
        if (validate()) {
            //this.model = tempModel;
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

    private void updateModelData() {
        model.setQuestion_diff(txt_q_diff.getText());
        model.setQuestion_weight(txt_q_weight.getText());
        model.setExpected_time(txt_q_exp_time.getText());

        for (AddQuestionAnswerRowController answerRowController : answerRowControllers) {
            model.getAnswers()
                    .get(answerRowControllers.indexOf(answerRowController))
                    .answer_text = answerRowController.txt_answer.getHtmlText();
        }

        for (AddQuestionContentRowController contentRowController : contentHepler.getItems()) {
            if (!contentRowController.rowContainer.isVisible()) {
                // Todo: Test Swapping contents possibelity.
                model.getContents().remove(contentHepler.indexOf(contentRowController));
                continue;
            }
            model.getContents().get(contentHepler.indexOf(contentRowController))
                    .setContent(contentRowController.content.getHtmlText());
        }
    }

    public void onAddContentClicked(ActionEvent e) throws IOException {
        contentHepler.add(null);
    }


    public void onAddAnswerClicked(ActionEvent e) throws IOException {
        addAnswerRow(null);
    }

    private void addAnswerRow(Answer answer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddQuestionAnswerRow.fxml"));
        Parent root = loader.load();
        ui_answers_list.getChildren().add((root));
        answerRowControllers.add((loader.getController()));
        AddQuestionAnswerRowController answerRowController = (AddQuestionAnswerRowController) loader.getController();

        if (answer == null) {
            Answer temp_answer = new Answer();
            temp_answer.answer_text = "";
            model.getAnswers().add(temp_answer);
        } else {
            answerRowController.txt_answer.setHtmlText(answer.answer_text);
        }

        answerRowController.label.setText((char) (65 + (answerRowControllers.size() - 1)) + "");
        answerRowController.addQuestionController = this;
        answerRowController.loader = loader;
    }

    public void removeAnswerRow(AddQuestionAnswerRowController ans) {
        model.getAnswers().remove(answerRowControllers.indexOf(ans));
        for (AddQuestionContentRowController contentRowController : contentHepler.getItems()) {
            Answer[] rightAnswers = new Answer[]{};
            model.getContents().get(contentHepler.indexOf(contentRowController)).getRightAnswers().toArray(rightAnswers);
            for (Answer rightAnswer : rightAnswers) {
                for (Answer answer : model.getAnswers()) {
                    if (answer == rightAnswer) {
                        model.getContents().get(contentHepler.indexOf(contentRowController)).getRightAnswers().remove(rightAnswer);
                    }
                }
            }
        }
        answerRowControllers.remove(ans);
        ui_answers_list.getChildren().remove((Node) ans.loader.getRoot());

        int count = 0;
        for (AddQuestionAnswerRowController a : answerRowControllers) {
            a.label.setText(((char) (65 + count) + ""));
            count++;
        }
    }

//    public void onRightAnswerChecked(AddQuestionAnswerRowController answerRowController, Boolean newValue) {
//        if (newValue) {
//            for (AddQuestionContentRowController contentRowController : contentHepler.getItems()) {
//                if (contentRowController.select.isSelected()) {
//                    model.getContents().get(contentHepler.indexOf(contentRowController)).getRightAnswers().clear();
//                    for (AddQuestionAnswerRowController answerRowController1 : answerRowControllers) {
//                        if (answerRowController1.getSelected()) {
//                            Answer answer = model.getAnswers().get(answerRowControllers.indexOf(answerRowController1));
//                            model.getContents().get(contentHepler.indexOf(contentRowController)).getRightAnswers()
//                                    .add(answer);
//                        }
//                    }
//
//
//                }
//            }
//        } else {
//            for (AddQuestionContentRowController contentRowController : contentHepler.getItems()) {
//                if (contentRowController.select.isSelected()) {
//                    Answer[] tempAnswers = new Answer[]{};
//                    model.getContents().get(contentHepler.indexOf(contentRowController)).getRightAnswers().toArray(tempAnswers);
//                    for (Answer rightAnswer : tempAnswers) {
//                        for (Answer answer : model.getAnswers()) {
//                            if (answer == rightAnswer) {
//                                model.getContents().get(contentHepler.indexOf(contentRowController)).getRightAnswers().remove(rightAnswer);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }


    private boolean validate() {
        for (QuestionContent questionContent : model.getContents()) {
            boolean isThereRightAnswer = false;
            if (Jsoup.parse(questionContent.getContent()).text().isEmpty() &&
                    Jsoup.parse(questionContent.getContent()).getElementsByTag("img").size() == 0)
                return false;
            for (Answer answer : model.getAnswers()) {
                if (Jsoup.parse(answer.answer_text).text().isEmpty() &&
                        Jsoup.parse(answer.answer_text).getElementsByTag("img").size() == 0)
                    return false;
            }
            if (questionContent.getRightAnswers().size() == 0) {
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


    private final ChangeListener<Boolean> MCQListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isNowSelected) {
            if (isNowSelected)
                model.setQuestion_type(QuestionType.MCQ);
        }
    };


    private final ChangeListener<Boolean> TrueFalseListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isNowSelected) {
            add_answer.setVisible(!isNowSelected);
            add_answer.setManaged(!isNowSelected);
            if (isNowSelected) {
                model.setQuestion_type(QuestionType.TRUE_FALSE);
//                questionContentAnswerToggleGroup.getToggles().clear();


                for (int i = 0; i < answerRowControllers.size(); i++) {
                    String innerText = "";
                    if (i == 0) {
                        innerText = "True";
                    } else if (i == 1) {
                        innerText = "False";
                    } else {
                        break;
                    }
                    String full = answerRowControllers.get(i).txt_answer.getHtmlText();
                    Document html = Jsoup.parse(full);
                    Element body = html.body();
                    body.children().clear();
                    body.html(innerText);
                    System.out.println(html.html());
                    answerRowControllers.get(i).txt_answer.setHtmlText(html.html());
                    answerRowControllers.get(i).txt_answer.setDisable(true);
                    answerRowControllers.get(i).remove_answer.setVisible(false);
                    answerRowControllers.get(i).remove_answer.setManaged(false);
                    answerRowControllers.get(i).setSingleMode(questionContentAnswerToggleGroup);

                }

                for (int i = answerRowControllers.size() -1 ; i > 1; i--) {
                    removeAnswerRow(answerRowControllers.get(i));
                }

            } else {
//                questionContentAnswerToggleGroup.getToggles().clear();
                for (AddQuestionAnswerRowController answerRowController : answerRowControllers) {
                    answerRowController.setMultiMode();

                    answerRowController.txt_answer.setDisable(false);
                    answerRowController.remove_answer.setVisible(true);
                    answerRowController.remove_answer.setManaged(true);
                }
            }
        }
    };

    private final ChangeListener<Boolean> extendedMatchListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isNowSelected) {
            add_question_content.setVisible(isNowSelected);
            add_question_content.setManaged(isNowSelected);
            if (isNowSelected) {
                model.setQuestion_type(QuestionType.EXTENDED_MATCH);
            } else {

                AddQuestionContentRowController[] tempList = new AddQuestionContentRowController[contentHepler.getItems().size()];
                contentHepler.getItems().toArray(tempList);
                for (AddQuestionContentRowController contentRowController : tempList) {
                    if (!contentRowController.select.isSelected()) {
                        contentHepler.remove(contentRowController);
                    }
                }
            }
        }
    };


    public class ContentHepler {
        private final List<AddQuestionContentRowController> contentRowControllers;

        public ContentHepler() {
            contentRowControllers = new ArrayList<>();
        }

        public List<AddQuestionContentRowController> getItems() {
            return this.contentRowControllers;
        }

        public void selectItem(int index) {
            contentRowControllers.get(index).select.setSelected(true);
        }

        public void add(QuestionContent questionContent) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddQuestionContentRow.fxml"));
            Parent root = loader.load();
            ui_question_contents_list.getChildren().add((root));
            contentRowControllers.add((loader.getController()));
            AddQuestionContentRowController contentRowController = (AddQuestionContentRowController) loader.getController();
            contentRowController.parent = this;
            contentRowController.select.setToggleGroup(questionContentToggleGroup);
            contentRowController.loader = loader;


            if (questionContent == null) {
                QuestionContent tempQuestionContent = new QuestionContent();
                //tempQuestionContent.setId(contentRowController.getNewFake_id() + "");
                tempQuestionContent.setContent("");
                tempQuestionContent.setRightAnswers(new ArrayList<>());
                model.getContents().add(tempQuestionContent);
            } else {
                contentRowController.content.setHtmlText(questionContent.getContent());
            }

            contentRowController.select.setSelected(true);
        }

        public void remove(AddQuestionContentRowController contentRowController) {
            boolean isSelected = contentRowController.select.isSelected();
            int index = indexOf(contentRowController);

            if (contentRowControllers.size() > 1) {
                model.getContents().remove(index);
                ui_question_contents_list.getChildren().remove((Node) contentRowController.loader.getRoot());
                contentRowControllers.remove(contentRowController);
                if (isSelected) {
                    selectItem(Math.max(0, index - 1));
                }
            }
        }

        public int indexOf(AddQuestionContentRowController contentRowController) {
            return contentRowControllers.indexOf(contentRowController);
        }

//        public int indexOf(QuestionContent questionContent) {
//            return model.getContents().indexOf(questionContent);
//        }

        public int getCurrentSeletedIndex() {
            for (AddQuestionContentRowController contentRowController : contentRowControllers) {
                if (contentRowController.select.isSelected())
                    return indexOf(contentRowController);
            }
            return -1;
        }
//
//        public AddQuestionContentRowController getCurrentSeletedItem() {
//            return null;
//        }


        public void updateContentRowAnswersUI(AddQuestionContentRowController contentRowController, Boolean newValue) {

            if (newValue) {
                List<Answer> rightAnswerslist = model.getContents().get(indexOf(contentRowController))
                        .getRightAnswers();

                for (int i = 0; i < model.getAnswers().size(); i++) {
                    if (rightAnswerslist.contains(model.getAnswers().get(i))) {
                        answerRowControllers.get(i).setSelected(true);
                    } else {
                        answerRowControllers.get(i).setSelected(false);
                    }
                }
            } else {
                model.getContents().get(indexOf(contentRowController)).getRightAnswers().clear();
                for (int i = 0; i < answerRowControllers.size(); i++) {
                    if (answerRowControllers.get(i).getSelected()) {
                        model.getContents().get(indexOf(contentRowController)).getRightAnswers().add(model.getAnswers().get(i));
                    }
                }
            }
        }

    }

}


