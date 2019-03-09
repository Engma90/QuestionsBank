package models;

import controllers.DashboardController;

import java.text.MessageFormat;

public class GeneratorHandler {
    public boolean Add(ExamModel model) {
        //DBHandler db = new DBHandler();

        String sql =
                "INSERT INTO exam (Date, ExamName, ExamModel, ExamCategory, CourseName, College, " +
                        "Department, Note, ExamType, Duration, TotalMarks, Course_idCourse, Doctor_idDoctor) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
        String[] params = new String[]{
                 model.Date, model.ExamName, model.ExamModel, model.ExamCategory,
                model.CourseName, model.College, model.Department, model.Note,
                model.ExamType, model.Duration, model.TotalMarks,
                DashboardController.current_selected_course_id, DashboardController.current_selected_dr_id
        };
        int exam_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);
        for (QuestionModel q : model.questionModelList) {

//            sql = MessageFormat.format("UPDATE question SET IsInExam = 1 WHERE idQuestion ={0};", q.getId());
//            DBSingletonHandler.getInstance().execute_sql(sql);
            sql = MessageFormat.format(
                    "INSERT INTO examquestion (Exam_idExam,Question_idQuestion) " +
                            "VALUES ({0},{1});"
                    , exam_id, q.getId());
            DBSingletonHandler.getInstance().execute_sql(sql);
        }

        return true;
    }
}
