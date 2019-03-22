package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class QuestionsTableHandler {
    private ObservableList<Question> questionList;

    public boolean Add(Topic topic, Question model) {
        //DBHandler db = new DBHandler();
        String sql = "insert into question (QuestionContent, QuestionType, QuestionDifficulty, " +
                "QuestionWeight, QuestionExpectedTime, Topic_idTopic) values (?,?,?,?,?,?);";
        int last_inserted_question_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql, new String[]
                {model.getQuestion_text(), model.getQuestion_type(), model.getQuestion_diff(), model.getQuestion_weight(), model.getExpected_time(),
                        topic.id});
        for (int i = 0; i < model.getAnswers().size(); i++) {
            sql = MessageFormat.format(
                    "insert into questionanswer (AnswerLabel, AnswerContent, Question_idQuestion, IsRightAnswer" +
                            ") values (\"{0}\",\"{1}\",{2},\"{3}\");"
                    , ((char) (65 + i) + ""), model.getAnswers().get(i).answer_text, last_inserted_question_id,
                    model.getAnswers().get(i).is_right_answer);
            boolean success = DBSingletonHandler.getInstance().execute_sql(sql);
        }
        return true;
    }


    public boolean Edit(Question model) {
        //DBHandler db = new DBHandler();
            String sql = "UPDATE question SET QuestionContent = ?, QuestionType = ?, QuestionDifficulty = ?, " +
                    "QuestionWeight =?, QuestionExpectedTime =? WHERE idQuestion =?;";

            int last_inserted_question_id = DBSingletonHandler.getInstance().execute_PreparedStatement(sql, new String[]
                    {model.getQuestion_text(), model.getQuestion_type(), model.getQuestion_diff(), model.getQuestion_weight(), model.getExpected_time(), model.getId()});

            sql = MessageFormat.format("DELETE FROM questionanswer WHERE Question_idQuestion = {0};", model.getId());
            boolean success = DBSingletonHandler.getInstance().execute_sql(sql);


            for (int i = 0; i < model.getAnswers().size(); i++) {
                sql = MessageFormat.format(
                        "insert into questionanswer (AnswerLabel, AnswerContent, Question_idQuestion, IsRightAnswer" +
                                ") values (\"{0}\",\"{1}\",{2},\"{3}\");"
                        , ((char) (65 + i) + ""), model.getAnswers().get(i).answer_text, model.getId(),
                        model.getAnswers().get(i).is_right_answer);
                success = DBSingletonHandler.getInstance().execute_sql(sql);
            }
            return true;

    }
    public boolean DeleteQuestionAnswers(String Q_id){

        //DBHandler db = new DBHandler();

        String sql = MessageFormat.format("DELETE FROM questionanswer WHERE Question_idQuestion = {0};", Q_id);
        boolean success = DBSingletonHandler.getInstance().execute_sql(sql);
        return success;

    }

    public boolean DeleteQuestion( Question model){
        //DBHandler db = new DBHandler();
            boolean success1 = DeleteQuestionAnswers(model.getId());
            String sql = MessageFormat.format("DELETE FROM question  WHERE idQuestion = {0};", model.getId());
            boolean success2 = DBSingletonHandler.getInstance().execute_sql(sql);
            return success1 && success2;

    }
    public boolean DeleteAllSelectedChapterQuestions(){
        for (Question q: questionList){
            System.out.println("Q_ID = "+q.getId());
            DeleteQuestion(q);
        }
        return true;
    }


    public ObservableList<Question> getQuestionList(Topic topic) {
        questionList = FXCollections.observableArrayList();
                String sql = MessageFormat.format(
                        "SELECT idQuestion,QuestionContent,QuestionDifficulty,QuestionType,QuestionWeight,QuestionExpectedTime FROM  question WHERE Topic_idTopic = {0};"
                , topic.id);
        ResultSet rs = DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next()) {

                    Question model = new Question(rs.getInt("idQuestion") + "", rs.getString("QuestionContent"), rs.getString("QuestionDifficulty"),
                            rs.getString("QuestionType"), rs.getString("QuestionWeight"),"");
                    model.setExpected_time(rs.getString("QuestionExpectedTime"));
                    questionList.add(model);
                    getQuestionAnswersList(model, DBSingletonHandler.getInstance());
            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //db.closeConnection();
        }

    }


    public ObservableList<Question> getQuestionList(Topic topic, String diff) {
        questionList = FXCollections.observableArrayList();
        String sql = MessageFormat.format(
                "SELECT idQuestion,QuestionContent,QuestionDifficulty,QuestionType,QuestionWeight,QuestionExpectedTime FROM  question WHERE Topic_idTopic = {0} AND QuestionDifficulty IN (0, {1});"
                , topic.id, diff);
        ResultSet rs = DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next()) {

                Question model = new Question(rs.getInt("idQuestion") + "", rs.getString("QuestionContent"), rs.getString("QuestionDifficulty"),
                        rs.getString("QuestionType"), rs.getString("QuestionWeight"),"");
                model.setExpected_time(rs.getString("QuestionExpectedTime"));
                questionList.add(model);
                getQuestionAnswersList(model, DBSingletonHandler.getInstance());
            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //db.closeConnection();
        }

    }


    public ObservableList<Question> getQuestionList(String ch_id) {
        questionList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        System.out.println("------------------------------------------0");
        String sql = MessageFormat.format(
                "SELECT * FROM ((question a " +
                        "JOIN topic b ON a.Topic_idTopic = b.idTopic) " +
                        "JOIN chapter c ON b.Chapter_idChapter = c.idChapter) " +
                        "where idChapter = {0};"
                , ch_id);
        ResultSet rs = DBSingletonHandler.getInstance().execute_query(sql);
        System.out.println("------------------------------------------1");
        try {
            while (rs.next()) {

                Question model = new Question(rs.getInt("idQuestion") + "", rs.getString("QuestionContent"), rs.getString("QuestionDifficulty"),
                        rs.getString("QuestionType"), rs.getString("QuestionWeight"),"");
                model.setExpected_time(rs.getString("QuestionExpectedTime"));
                questionList.add(model);
                System.out.println("------------------------------------------2");

                getQuestionAnswersList(model, DBSingletonHandler.getInstance());

            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //db.closeConnection();
        }

    }

    public ObservableList<Question> getQuestionList(String ch_id, String diff) {
        questionList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        System.out.println("------------------------------------------0");
        String sql = MessageFormat.format(
                "SELECT * FROM ((question a " +
                        "JOIN topic b ON a.Topic_idTopic = b.idTopic) " +
                        "JOIN chapter c ON b.Chapter_idChapter = c.idChapter) " +
                        "where idChapter = {0} AND QuestionDifficulty <= \"{1}\";"
                , ch_id, diff);
        ResultSet rs = DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next()) {

                Question model = new Question(rs.getInt("idQuestion") + "", rs.getString("QuestionContent"), rs.getString("QuestionDifficulty"),
                        rs.getString("QuestionType"), rs.getString("QuestionWeight"),"");
                model.setExpected_time(rs.getString("QuestionExpectedTime"));
                questionList.add(model);
                System.out.println("------------------------------------------2");
                getQuestionAnswersList(model, DBSingletonHandler.getInstance());

            }
            return questionList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //db.closeConnection();
        }

    }
    public ObservableList<Question> getCachedList(){
        return this.questionList;
    }

    private void getQuestionAnswersList(Question model, DBSingletonHandler db) {
        try {
            String sql = MessageFormat.format(
                    "SELECT * FROM questionanswer where Question_idQuestion = {0};"
                    , model.getId());
            ResultSet rs_ans = db.execute_query(sql);
            List<Answer> temp_array;
            temp_array = new ArrayList<>();
            while (rs_ans.next()) {
                Answer answer = new Answer();
                answer.answer_text = rs_ans.getString("AnswerContent");
                answer.is_right_answer = rs_ans.getInt("isRightAnswer");
                temp_array.add(answer);
            }
            model.setAnswers(temp_array);
        } catch (Exception ex) {

        }

    }
}

