package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CourseModel {
    public String id;
    public String name;
    public String code;

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
