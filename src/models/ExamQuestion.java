package models;

import java.util.ArrayList;
import java.util.List;

public class ExamQuestion {
    private String id;
    private String QuestionContent;
    private String QuestionType;
    private String QuestionDifficulty;
    private String QuestionWeight;
    private String QuestionExpectedTime;
    private String ExamModel_idExamModel;
//    private String CourseName;
//    private String CourseCode;
//    private String CourseLevel;
//    private String ChapterName;
//    private String ChapterNumber;

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    private List<Answer> answers = new ArrayList<>();

    public ExamQuestion() {
    }

//    private String TopicName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionContent() {
        return QuestionContent;
    }

    public void setQuestionContent(String questionContent) {
        QuestionContent = questionContent;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public String getQuestionDifficulty() {
        return QuestionDifficulty;
    }

    public void setQuestionDifficulty(String questionDifficulty) {
        QuestionDifficulty = questionDifficulty;
    }

    public String getQuestionWeight() {
        return QuestionWeight;
    }

    public void setQuestionWeight(String questionWeight) {
        QuestionWeight = questionWeight;
    }

    public String getQuestionExpectedTime() {
        return QuestionExpectedTime;
    }

    public void setQuestionExpectedTime(String questionExpectedTime) {
        QuestionExpectedTime = questionExpectedTime;
    }

    public String getExamModel_idExamModel() {
        return ExamModel_idExamModel;
    }

    public void setExamModel_idExamModel(String examModel_idExamModel) {
        ExamModel_idExamModel = examModel_idExamModel;
    }

//    public String getCourseName() {
//        return CourseName;
//    }
//
//    public void setCourseName(String courseName) {
//        CourseName = courseName;
//    }
//
//    public String getCourseCode() {
//        return CourseCode;
//    }
//
//    public void setCourseCode(String courseCode) {
//        CourseCode = courseCode;
//    }
//
//    public String getCourseLevel() {
//        return CourseLevel;
//    }
//
//    public void setCourseLevel(String courseLevel) {
//        CourseLevel = courseLevel;
//    }
//
//    public String getChapterName() {
//        return ChapterName;
//    }
//
//    public void setChapterName(String chapterName) {
//        ChapterName = chapterName;
//    }
//
//    public String getChapterNumber() {
//        return ChapterNumber;
//    }
//
//    public void setChapterNumber(String chapterNumber) {
//        ChapterNumber = chapterNumber;
//    }
//
//    public String getTopicName() {
//        return TopicName;
//    }
//
//    public void setTopicName(String topicName) {
//        TopicName = topicName;
//    }
}
