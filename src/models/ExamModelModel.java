package models;

import java.util.List;

public class ExamModelModel {
    private String id;
    private String Exam_idExam;
    private String ExamModelNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam_idExam() {
        return Exam_idExam;
    }

    public void setExam_idExam(String exam_idExam) {
        Exam_idExam = exam_idExam;
    }

    public String getExamModelNumber() {
        return ExamModelNumber;
    }

    public void setExamModelNumber(String examModelNumber) {
        ExamModelNumber = examModelNumber;
    }

    public List<ExamQuestionModel> getExamQuestionsList() {
        return ExamQuestionsList;
    }

    public void setExamQuestionsList(List<ExamQuestionModel> examQuestionsList) {
        ExamQuestionsList = examQuestionsList;
    }

    private List<ExamQuestionModel> ExamQuestionsList;

    public ExamModelModel() {
    }
}
