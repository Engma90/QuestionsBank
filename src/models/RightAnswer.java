package models;

public class RightAnswer {
    private String id;
    private Answer answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public QuestionContent getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    private QuestionContent questionContent;


}
