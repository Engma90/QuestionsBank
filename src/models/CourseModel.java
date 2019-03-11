package models;

public class CourseModel {
    public String id;
    public String name;
    public String code;
    public String level;

    public CourseModel(){
        this("-1", "", "","");
    }
    public CourseModel(String id, String name, String code, String level) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.level = level;
    }
    @Override
    public String toString(){
        return this.name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
