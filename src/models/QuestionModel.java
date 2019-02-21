package models;

import javafx.beans.property.SimpleStringProperty;

public class QuestionModel {

    private SimpleStringProperty question = new SimpleStringProperty("");
    private SimpleStringProperty diff = new SimpleStringProperty("");
    private SimpleStringProperty type = new SimpleStringProperty("");
    private char right_answer;
    private String[] answers = new String[4];

    public QuestionModel(){
        this("", "", "");
    }
    public QuestionModel(String q, String d, String type){
        setQuestion(q);
        setDiff(d);
        setType(type);

    }


    public String getQuestion() {
        return question.get();
    }
    public void setQuestion(String q){
        this.question.set(q);
    }

    public String getDiff() {
        return diff.get();
    }

    public void setDiff(String diff) {
        this.diff.set(diff);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public char getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(char right_answer) {
        this.right_answer = right_answer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }




}
