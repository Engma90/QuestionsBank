package models;

import javafx.beans.property.SimpleStringProperty;

public class QuestionModel {
    int id;
    private SimpleStringProperty question_text = new SimpleStringProperty("");
    private SimpleStringProperty question_diff = new SimpleStringProperty("");
    private SimpleStringProperty question_type = new SimpleStringProperty("");
    private char right_answer;
    private String[] answers = new String[4];

    public QuestionModel(){
        this("", "", "");
    }
    public QuestionModel(String q, String d, String question_type){
        setQuestion_text(q);
        setQuestion_diff(d);
        setQuestion_type(question_type);

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


    public void add_question_to_db(){

    }
    public void edit_question_in_db(){

    }
    public void delete_question_from_db(){

    }
}
