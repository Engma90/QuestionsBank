package models;

public class ChapterModel {
    public String id;
    public String number;
    public String name;

    public ChapterModel(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }
    public ChapterModel(){
        this("-1","","");
    }

    @Override
    public String toString(){
        return this.name;
    }
}
