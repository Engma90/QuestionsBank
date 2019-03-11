package models;

import java.util.ArrayList;
import java.util.List;

public class ExamModel {
    private String id;
    private String Doctor_idDoctor;
    private String Date;
    private String ExamName;
    private String ExamCategory;
    private String CourseName;
    private String College;
    private String Department;
    private String Note;
    private String ExamType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor_idDoctor() {
        return Doctor_idDoctor;
    }

    public void setDoctor_idDoctor(String doctor_idDoctor) {
        Doctor_idDoctor = doctor_idDoctor;
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

    public String getExamCategory() {
        return ExamCategory;
    }

    public void setExamCategory(String examCategory) {
        ExamCategory = examCategory;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getExamType() {
        return ExamType;
    }

    public void setExamType(String examType) {
        ExamType = examType;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getTotalMarks() {
        return TotalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        TotalMarks = totalMarks;
    }

    public String getExamLanguage() {
        return ExamLanguage;
    }

    public void setExamLanguage(String examLanguage) {
        ExamLanguage = examLanguage;
    }

    public String getCourseCategory() {
        return CourseCategory;
    }

    public void setCourseCategory(String courseCategory) {
        CourseCategory = courseCategory;
    }

    public String getCourseCode() {
        return CourseCode;
    }

    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }

    public List<ExamModelModel> getExamModelModelList() {
        return examModelModelList;
    }

    public void setExamModelModelList(List<ExamModelModel> examModelModelList) {
        this.examModelModelList = examModelModelList;
    }

    private String Duration;
    private String TotalMarks;
    private String ExamLanguage;
    private String CourseCategory;
    private String CourseCode;
    private List<ExamModelModel> examModelModelList;


    public ExamModel() {

    }




}
