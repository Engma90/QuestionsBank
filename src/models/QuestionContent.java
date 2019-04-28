package models;

import java.util.ArrayList;
import java.util.List;

public class QuestionContent {
    private String id;
    private String content;
    private List<Answer> RightAnswers = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Answer> getRightAnswers() {
        return RightAnswers;
    }

    public void setRightAnswers(List<Answer> rightAnswers) {
        RightAnswers = rightAnswers;
    }




}
