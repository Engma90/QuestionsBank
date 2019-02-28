package models;

import controllers.DashboardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class QuestionTableHandler {
    private ObservableList<QuestionModel> questionList;

    public boolean Add(String q, String diff, String weight, String type, String[] answers, String right_answer) {
        DBHandler db = new DBHandler();
        String sql = "insert into question (QuestionContent, QuestionType, QuestionDifficulty, " +
                "QuestionWeight, Chapter_idChapter) values (?,?,?,?,?);";
        int last_inserted_question_id = db.execute_PreparedStatement(sql, new String[]
                {q, type, diff, weight, DashboardController.current_selected_chapter_id});
        for (int i = 0; i < answers.length; i++) {
            sql = MessageFormat.format(
                    "insert into answer (AnswerLabel, AnswerContent, Question_idQuestion, IsRightAnswer" +
                            ") values (\"{0}\",\"{1}\",{2},\"{3}\");"
                    , ((char) (65 + i) + ""), answers[i], last_inserted_question_id,
                    ((char) (65 + i) + "").equals(right_answer) ? 1 : 0);
            boolean success = db.execute_sql(sql);
        }
        return true;
    }


    public boolean Edit(String id,String q, String diff, String weight, String type, String[] answers, String right_answer) {
        DBHandler db = new DBHandler();
        String sql = "UPDATE question SET QuestionContent = ?, QuestionType = ?, QuestionDifficulty = ?, " +
                "QuestionWeight =? WHERE idQuestion =?;";

        int last_inserted_question_id = db.execute_PreparedStatement(sql, new String[]
                {q, type, diff, weight, id});

        sql = MessageFormat.format("DELETE FROM answer WHERE Question_idQuestion = {0};", id);
        boolean success = db.execute_sql(sql);


        for (int i = 0; i < answers.length; i++) {
            sql = MessageFormat.format(
                    "insert into answer (AnswerLabel, AnswerContent, Question_idQuestion, IsRightAnswer" +
                            ") values (\"{0}\",\"{1}\",{2},\"{3}\");"
                    , ((char) (65 + i) + ""), answers[i], id,
                    ((char) (65 + i) + "").equals(right_answer) ? 1 : 0);
            success = db.execute_sql(sql);
        }
        return true;
    }
    public boolean DeleteQuestionAnswers(String Q_id){
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format("DELETE FROM answer WHERE Question_idQuestion = {0};", Q_id);
        boolean success = db.execute_sql(sql);
        return success;

    }

    public boolean DeleteQuestion(String Q_id){
        DBHandler db = new DBHandler();
        boolean success1 = DeleteQuestionAnswers(Q_id);
        String sql = MessageFormat.format("DELETE FROM question  WHERE idQuestion = {0};", Q_id);
         boolean success2 = db.execute_sql(sql);

        return  success1 && success2;
    }
    public boolean DeleteAllSelectedChapterQuestions(){
        for (QuestionModel q:questionList){
            System.out.println("Q_ID = "+q.getId());
            DeleteQuestion(q.getId());
        }
        return true;
    }


    public ObservableList<QuestionModel> getQuestionList() {
        questionList = FXCollections.observableArrayList();
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT idQuestion,QuestionContent,QuestionDifficulty,QuestionType,QuestionWeight FROM ((( question  " +
                        "INNER JOIN chapter ON idChapter = {0}) " +
                        "INNER JOIN course ON idCourse = {1})" +
                        "INNER JOIN doctor ON  idDoctor ={2}) WHERE Chapter_idChapter = {0};"
                , DashboardController.current_selected_chapter_id
                , DashboardController.current_selected_course_id, DashboardController.current_selected_dr_id);
        ResultSet rs = db.execute_query(sql);
        try {
            while (rs.next()) {
                QuestionModel model = new QuestionModel(rs.getInt("idQuestion") + "", rs.getString("QuestionContent"), rs.getString("QuestionDifficulty"),
                        rs.getString("QuestionType"), rs.getString("QuestionWeight"));
                questionList.add(model);
                getQuestionAnswersList(model, db);
            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            db.closeConnection();
        }

    }


    public ObservableList<QuestionModel> getQuestionList(String ch_id) {
        questionList = FXCollections.observableArrayList();
        DBHandler db = new DBHandler();
        System.out.println("------------------------------------------0");
        String sql = MessageFormat.format(
                "SELECT idQuestion,QuestionContent,QuestionDifficulty,QuestionType,QuestionWeight FROM ((( question  " +
                        "INNER JOIN chapter ON idChapter = {0}) " +
                        "INNER JOIN course ON idCourse = {1})" +
                        "INNER JOIN doctor ON  idDoctor ={2}) WHERE Chapter_idChapter = {0};"
                , ch_id
                , DashboardController.current_selected_course_id, DashboardController.current_selected_dr_id);
        ResultSet rs = db.execute_query(sql);
        System.out.println("------------------------------------------1");
        try {
            while (rs.next()) {
                QuestionModel model = new QuestionModel(rs.getInt("idQuestion") + "", rs.getString("QuestionContent"), rs.getString("QuestionDifficulty"),
                        rs.getString("QuestionType"), rs.getString("QuestionWeight"));
                questionList.add(model);
                System.out.println("------------------------------------------2");

                getQuestionAnswersList(model, db);
            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            db.closeConnection();
        }

    }

    private void getQuestionAnswersList(QuestionModel model, DBHandler db) {
        try {
            String sql = MessageFormat.format(
                    "SELECT * FROM answer where Question_idQuestion = {0};"
                    , model.getId());
            ResultSet rs_ans = db.execute_query(sql);
            int counter = 0;
            String[] temp_array;
            if (model.getQuestion_type().equals("MCQ"))
                temp_array = new String[4];
            else temp_array = new String[2];

            while (rs_ans.next()) {
                temp_array[counter] = rs_ans.getString("AnswerContent");
                if (rs_ans.getInt("isRightAnswer") == 1) {
                    model.setRight_answer(rs_ans.getString("AnswerLabel"));
                }

                counter++;
            }
            model.setAnswers(temp_array);
        } catch (Exception ex) {

        }

    }
}
