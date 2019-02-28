package models;

import javafx.beans.property.SimpleStringProperty;

public class QuestionModel {


    private String id;
    private SimpleStringProperty question_text = new SimpleStringProperty("");
    private SimpleStringProperty question_diff = new SimpleStringProperty("");
    private SimpleStringProperty question_type = new SimpleStringProperty("");

    public String getQuestion_weight() {
        return question_weight.get();
    }

    public SimpleStringProperty question_weightProperty() {
        return question_weight;
    }

    public void setQuestion_weight(String question_weight) {
        this.question_weight.set(question_weight);
    }

    private SimpleStringProperty question_weight = new SimpleStringProperty("");
    private String right_answer;
    private String[] answers = new String[4];

    public QuestionModel(){
        this("", "", "", "", "");
    }
    public QuestionModel(String id, String q, String d, String question_type, String w){
        this.id = id;
        setQuestion_text(q);
        setQuestion_diff(d);
        setQuestion_type(question_type);
        setQuestion_weight(w);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getQuestion_text() {
        return question_text.get().toString();
    }
    public void setQuestion_text(String q){
        this.question_text.set(q);
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

    public String getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(String right_answer) {
        this.right_answer = right_answer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
}