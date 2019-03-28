package models;

public class Course {
    public String id;
    public String name;
    public String code;
    public String level;

    public String year;

    public Course(){
        this("-1", "", "","","");
    }
    public Course(String id, String name, String code, String level, String year) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.level = level;
        this.year = year;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
