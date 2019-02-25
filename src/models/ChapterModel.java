package models;

public class ChapterModel {
    public String id;
    public String name;

    public ChapterModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
