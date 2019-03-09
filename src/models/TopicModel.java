package models;

public class TopicModel {

    public TopicModel(){

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
