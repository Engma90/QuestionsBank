package models;

public class Chapter {
    public String id;
    public String number;
    public String name;

    public Chapter(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }
    public Chapter(){
        this("-1","","");
    }

    @Override
    public String toString(){
        return this.name;
    }
}
