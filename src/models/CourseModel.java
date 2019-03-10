package models;

public class CourseModel {
    public String id;
    public String name;
    public String code;

    public CourseModel(){
        this("-1", "", "");
    }
    public CourseModel(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
    @Override
    public String toString(){
        return this.name;
    }

}
