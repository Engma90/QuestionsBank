package models;

import controllers.DashboardController;

import java.text.MessageFormat;

public class GeneratorHandler {
    public boolean Add(ExamModel model) {
        DBHandler db = new DBHandler();

        String sql = MessageFormat.format(
                "INSERT INTO exam (Date, ExamName, ExamModel, ExamCategory, CourseName, College, " +
                        "Department, Note, ExamType, Duration, TotalMarks, Course_idCourse, Course_Doctor_idDoctor) " +
                        "VALUES (\"{0}\",\"{1}\",\"{2}\",\"{3}\", " +
                        "\"{4}\", \"{5}\", \"{6}\", \"{7}\", \"{8}\", \"{9}\", \"{10}\", {11}, {12}) ;"
                , model.Date, model.ExamName, model.ExamModel, model.ExamCategory,
                model.CourseName, model.College, model.Department, model.Note,
                model.ExamType, model.Duration, model.TotalMarks, DashboardController.current_selected_course_id,
                DashboardController.current_selected_dr_id );

        return db.execute_sql(sql);
    }
}
