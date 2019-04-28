package models;

public class GeneratorHandler {
    public boolean Add(Exam model) {
        //DBHandler db = new DBHandler();

        String sql =
                "INSERT INTO Exam (Date, ExamName, CourseName, College, " +
                        "Department, Note, ExamType, Duration, TotalMarks, ExamLanguage, CourseCode, CourseLevel, CourseYear, Doctor_idDoctor) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?) ;";
        String[] params = new String[]{
                model.getDate(), model.getExamName(),
                model.getCourseName(), model.getCollege(), model.getDepartment(), model.getNote(),
                model.getExamType(), model.getDuration(), model.getTotalMarks(), model.getExamLanguage(), model.getCourseCode()
                , model.getCourseLevel(), model.getYear(), model.getDoctor_idDoctor()
        };
        int exam_id = DBHandler.getInstance().execute_PreparedStatement(sql, params);
        model.setId(exam_id + "");

        for (ExamModel examModel : model.getExamModelList()) {
            examModel.setExam_idExam(exam_id + "");
            sql =
                    "INSERT INTO ExamModel (Exam_idExam,ExamModelNumber) " +
                            "VALUES (?,?);";
            params = new String[]{
                    examModel.getExam_idExam(), examModel.getExamModelNumber()};
            int model_id = DBHandler.getInstance().execute_PreparedStatement(sql, params);
            examModel.setId(model_id + "");
            for (Question q : examModel.getQuestionsList()) {
                sql =
                        "INSERT INTO ExamQuestion (QuestionType,QuestionDifficulty, QuestionWeight, QuestionExpectedTime" +
                                ", ExamModel_idExamModel) " +
                                "VALUES (?,?,?,?,?);";
                params = new String[]{q.getQuestion_type(), q.getQuestion_diff(), q.getQuestion_weight(), q.getExpected_time()
                        , examModel.getId()};
                int question_id = DBHandler.getInstance().execute_PreparedStatement(sql, params);
                q.setId(question_id + "");

                for (Answer ans : q.getAnswers()) {

                    sql =
                            "INSERT INTO ExamQuestionAnswer (AnswerContent, ExamQuestion_idQuestion) " +
                                    "VALUES (?,?);";
                    params = new String[]{ans.answer_text, q.getId()};
                    DBHandler.getInstance().execute_PreparedStatement(sql, params);
                }
                for (QuestionContent questionContent : q.getContents()) {
                    sql =
                            "INSERT INTO ExamQuestionContent (ExamQuestion_idQuestion, QuestionContent) " +
                                    "VALUES (?,?);";
                    params = new String[]{q.getId(), questionContent.getContent()};
                    int question_content_id = DBHandler.getInstance().execute_PreparedStatement(sql, params);
                    questionContent.setId(question_content_id + "");

                    for (Answer rightAnswer : questionContent.getRightAnswers()) {
                        sql =
                                "INSERT INTO ExamContentRightAnswer (ExamQuestionAnswer_idAnswer, ExamQuestionContent_idQuestionContent) " +
                                        "VALUES (?,?);";
                        params = new String[]{rightAnswer.id, questionContent.getId()};
                        DBHandler.getInstance().execute_PreparedStatement(sql, params);
                    }

                }


            }
        }

        return true;
    }
}
