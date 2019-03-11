package models;

import controllers.CoursesTableController;
import controllers.DashboardController;

import java.text.MessageFormat;

public class GeneratorHandler {
    public boolean Add(ExamModel model) {
        //DBHandler db = new DBHandler();

        String sql =
                "INSERT INTO exam (Date, ExamName, ExamCategory, CourseName, College, " +
                        "Department, Note, ExamType, Duration, TotalMarks, ExamLanguage, CourseCode, CourseCategory, Doctor_idDoctor) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?) ;";
        String[] params = new String[]{
                 model.getDate(), model.getExamName(), model.getExamCategory(),
                model.getCourseName(), model.getCollege(), model.getDepartment(), model.getNote(),
                model.getExamType(), model.getDuration(), model.getTotalMarks(), model.getExamLanguage(), model.getCourseCode()
                ,model.getCourseCategory(), model.getDoctor_idDoctor()
        };
        int exam_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);


        for(ExamModelModel examModelModel:model.getExamModelModelList()) {
            examModelModel.setExam_idExam(exam_id+"");
            sql =
                    "INSERT INTO exammodel (Exam_idExam,ExamModelNumber) " +
                            "VALUES (?,?);";
            params = new String[]{
                    examModelModel.getExam_idExam(), examModelModel.getExamModelNumber()};
            int model_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);

            for (ExamQuestionModel q : examModelModel.getExamQuestionsList()) {
                q.setExamModel_idExamModel(model_id+"");
                sql =
                        "INSERT INTO examquestion (QuestionContent,QuestionType,QuestionDifficulty, QuestionWeight, QuestionExpectedTime" +
                                ", ExamModel_idExamModel, CourseName, CourseCode, CourseCategory, ChapterName, ChapterNumber" +
                                ", TopicName ) " +
                                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
                params = new String[]{
                        q.getQuestionContent(), q.getQuestionType(), q.getQuestionDifficulty(), q.getQuestionWeight(), q.getQuestionExpectedTime()
                , q.getExamModel_idExamModel(), q.getCourseName(), q.getCourseCode(), q.getCourseCategory(), q.getChapterName(), q.getChapterNumber()
                , q.getTopicName()};
                int question_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);

                for (AnswerModel ans : q.getAnswers()) {

                    sql =
                            "INSERT INTO examquestionanswer (AnswerLabel, AnswerContent, IsRightAnswer, ExamQuestion_idQuestion) " +
                                    "VALUES (?,?,?,?);";
                    params = new String[]{"-", ans.answer_text, ans.is_right_answer+"", question_id+""};
                    DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);

                }

            }
        }

        return true;
    }
}
