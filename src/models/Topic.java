package models;

import java.util.List;

public class Topic {

    public Topic(){
        this("-1","");
    }
    public Topic(String id, String name) {
        this.id = id;
        this.name = name;
        this.AllQuestionsList = QuestionsTableHandler.getInstance().getQuestionList(this);
    }

    public String id;
    public String name;
    public List<Question> AllQuestionsList;

    @Override
    public String toString(){
        return this.name;
    }
}
