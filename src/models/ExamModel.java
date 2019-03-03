package models;

import java.util.List;

public class ExamModel {
    public String id;
    public String Date;
    public String ExamName;
    public String ExamModel;
    public String ExamCategory;
    public String CourseName;
    public String College;
    public String Department;
    public String Note;
    public String ExamType;
    public String Duration;
    public String TotalMarks;
    public String Course_idCourse;
    public String Year;
    public List<QuestionModel> questionModelList;


    public ExamModel(){
        this("","","","","");
    }
    public ExamModel(String id, String date, String examName, String examModel, String year) {
        this.id = id;
        Date = date;
        ExamName = examName;
        ExamModel = examModel;
        Year = year;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getExamName() {
        return ExamName;
    }

    public void setExamName(String examName) {
        ExamName = examName;
    }

    public String getExamModel() {
        return ExamModel;
    }

    public void setExamModel(String examModel) {
        ExamModel = examModel;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }



}
