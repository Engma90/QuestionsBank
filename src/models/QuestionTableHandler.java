package models;
import controllers.DashboardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class QuestionTableHandler {
    private ObservableList<QuestionModel> questionList;
    public boolean Add(String q, String diff, String weight, String type, String[] answers, String right_answer) {
        DBHandler db = new DBHandler();



        String sql = "insert into question (QuestionContent, QuestionType, QuestionDifficulty, " +
                       "QuestionWeight, Chapter_idChapter) values (?,?,?,?,?);";
//                MessageFormat.format(
//                "insert into question (QuestionContent, QuestionType, QuestionDifficulty, " +
//                        "QuestionWeight, Chapter_idChapter) values (\"{0}\",\"{1}\",\"{2}\",\"{3}\",{4});"
//                , q, type, diff, weight, DashboardController.current_selected_chapter_id);


        int last_inserted_question_id = db.execute_PreparedStatement(sql,new String[]
                {q,type,diff,weight,DashboardController.current_selected_chapter_id});
        for (int i=0; i<answers.length;i++){
            sql = MessageFormat.format(
                    "insert into answer (AnswerLabel, AnswerContent, Question_idQuestion, IsRightAnswer" +
                            ") values (\"{0}\",\"{1}\",{2},\"{3}\");"
                    ,((char)(65+i)+""), answers[i], last_inserted_question_id,
                    ((char)(65+i)+"").equals(right_answer)?1:0);
            boolean success = db.execute_sql(sql);
        }
        return true;
    }


    public ObservableList<QuestionModel> getQuestionList(){
        questionList = FXCollections.observableArrayList();
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT idQuestion,QuestionContent,QuestionDifficulty,QuestionType,QuestionWeight FROM ((( question  " +
                        "INNER JOIN chapter ON idChapter = {0}) " +
                        "INNER JOIN course ON idCourse = {1})" +
                        "INNER JOIN doctor ON  idDoctor ={2}) WHERE Chapter_idChapter = {0};"
                , DashboardController.current_selected_chapter_id
        , DashboardController.current_selected_course_id, DashboardController.current_selected_dr_id);
        ResultSet rs =  db.execute_query(sql);
        try {
            while (rs.next())
            {
                questionList.add(new QuestionModel(rs.getString("QuestionContent"),rs.getString("QuestionDifficulty"),
                        rs.getString("QuestionType"), rs.getString("QuestionWeight")));
            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            db.closeConnection();
        }

    }
}

