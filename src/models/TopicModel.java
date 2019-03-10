package models;

public class TopicModel {

    public TopicModel(){
        this("-1","");
    }
    public TopicModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String id;
    public String name;

    @Override
    public String toString(){
        return this.name;
    }
}
