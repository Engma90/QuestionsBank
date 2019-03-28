package models;

public class GeneratorHandler {
    public boolean Add(Exam model) {
        //DBHandler db = new DBHandler();

        String sql =
                "INSERT INTO exam (Date, ExamName, CourseName, College, " +
                        "Department, Note, ExamType, Duration, TotalMarks, ExamLanguage, CourseCode, CourseLevel, CourseYear, Doctor_idDoctor) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?) ;";
        String[] params = new String[]{
                 model.getDate(), model.getExamName(),
                model.getCourseName(), model.getCollege(), model.getDepartment(), model.getNote(),
                model.getExamType(), model.getDuration(), model.getTotalMarks(), model.getExamLanguage(), model.getCourseCode()
                ,model.getCourseLevel(), model.getYear(), model.getDoctor_idDoctor()
        };
        int exam_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);
        model.setId(exam_id+"");

        for(ExamModel examModel :model.getExamModelList()) {
            examModel.setExam_idExam(exam_id+"");
            sql =
                    "INSERT INTO exammodel (Exam_idExam,ExamModelNumber) " +
                            "VALUES (?,?);";
            params = new String[]{
                    examModel.getExam_idExam(), examModel.getExamModelNumber()};
            int model_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);
            examModel.setId(model_id+"");
            for (ExamQuestion q : examModel.getExamQuestionsList()) {
                q.setExamModel_idExamModel(model_id+"");
                sql =
                        "INSERT INTO examquestion (QuestionContent,QuestionType,QuestionDifficulty, QuestionWeight, QuestionExpectedTime" +
                                ", ExamModel_idExamModel) " +
                                "VALUES (?,?,?,?,?,?);";
                params = new String[]{
                        q.getQuestionContent(), q.getQuestionType(), q.getQuestionDifficulty(), q.getQuestionWeight(), q.getQuestionExpectedTime()
                , q.getExamModel_idExamModel()};
                int question_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);
                q.setId(question_id+"");
                for (Answer ans : q.getAnswers()) {

                    sql =
                            "INSERT INTO examquestionanswer (AnswerLabel, AnswerContent, IsRightAnswer, ExamQuestion_idQuestion) " +
                                    "VALUES (?,?,?,?);";
                    params = new String[]{"-", ans.answer_text, ans.is_right_answer+"", q.getId()};
                    DBSingletonHandler.getInstance().execute_PreparedStatement(sql,params);

                }

            }
        }

        return true;
    }
}
