package models;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Question {


    private String id;
    private SimpleStringProperty raw_text = new SimpleStringProperty("");
    private SimpleStringProperty question_diff = new SimpleStringProperty("");
    private SimpleStringProperty question_type = new SimpleStringProperty("");
    private SimpleStringProperty question_weight = new SimpleStringProperty("");
    private SimpleStringProperty expected_time = new SimpleStringProperty("");


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    private List<Answer> answers = new ArrayList<>();
    private List<QuestionContent> contents = new ArrayList<>();

    public List<ILO> getIlos() {
        return ilos;
    }

    public void setIlos(List<ILO> ilos) {
        this.ilos = ilos;
    }

    private List<ILO> ilos = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion_diff() {
        return question_diff.get().toString();
    }

    public void setQuestion_diff(String question_diff) {
        this.question_diff.set(question_diff);
    }

    public String getQuestion_type() {
        return question_type.get().toString();
    }

    public void setQuestion_type(String question_type) {
        this.question_type.set(question_type);
    }

    public String getExpected_time() {
        return expected_time.get();
    }

    public void setExpected_time(String expected_time) {
        this.expected_time.set(expected_time);
    }

    public List<QuestionContent> getContents() {
        return contents;
    }

    public void setContents(List<QuestionContent> contents) {
        this.contents = contents;
    }

    public String getRaw_text() {
        return raw_text.get();
    }

    public void setRaw_text(String raw_text) {
        this.raw_text.set(raw_text);
    }

    public String getQuestion_weight() {
        return question_weight.get();
    }

    public void setQuestion_weight(String question_weight) {
        this.question_weight.set(question_weight);
    }


    @Override
    public Question clone() {
//        Question clone = (Question) super.clone();
//        try {
//            super.clone();
//        } catch (CloneNotSupportedException e) {
//            //e.printStackTrace();
//        }
//        System.out.println("rightAnswerslist.size1: " + getContents().get(0).getRightAnswers().size());
        Question tempModel = new Question();
        tempModel.setId(this.getId());
        tempModel.setQuestion_diff(this.getQuestion_diff());
        tempModel.setQuestion_weight(this.getQuestion_weight());
        tempModel.setExpected_time(this.getExpected_time());
        tempModel.setQuestion_type(this.getQuestion_type());

        List<Answer> answers = new ArrayList<>();
        for (Answer answer : this.getAnswers()) {
            Answer answer1 = new Answer();
            answer1.answer_text = answer.answer_text;
            answer1.id = answer.id;
            answers.add(answer1);
        }
        tempModel.setAnswers(answers);

        List<QuestionContent> Contents = new ArrayList<>();
        for (QuestionContent questionContent : this.getContents()) {
            QuestionContent questionContent1 = new QuestionContent();
            questionContent1.setId(questionContent.getId());
            questionContent1.setContent(questionContent.getContent());
            Contents.add(questionContent1);
        }
        tempModel.setContents(Contents);

        for (QuestionContent questionContent:getContents()){
            List<Answer> rightAnswers = new ArrayList<>();
            for (Answer answer : tempModel.getAnswers()) {
                for (Answer rightAnswer : questionContent.getRightAnswers()) {
                    if (answer.id.equals(rightAnswer.id))
                        rightAnswers.add(answer);
                }
            }
            tempModel.getContents().get(getContents().indexOf(questionContent)).setRightAnswers(rightAnswers);
        }

        List<ILO> Ilos = new ArrayList<>();
        for (ILO ilo : this.getIlos()) {
            ILO ilo1 = new ILO();
            ilo1.setId(ilo.getId());
            ilo1.setCode(ilo.getCode());
            ilo1.setDescription(ilo.getDescription());
            Ilos.add(ilo1);
        }
        tempModel.setIlos(Ilos);


//        System.out.println("rightAnswerslist.size2: " + tempModel.getContents().get(0).getRightAnswers().size());
        return tempModel;
    }
}
