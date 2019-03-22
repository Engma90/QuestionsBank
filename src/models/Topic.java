package models;

public class Topic {

    public Topic(){
        this("-1","");
    }
    public Topic(String id, String name) {
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
