package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class QuestionsTableHandler {
    private ObservableList<Question> questionList;
    private static volatile QuestionsTableHandler instance = null;

    private QuestionsTableHandler() {

    }

    public static QuestionsTableHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (QuestionsTableHandler.class) {
                if (instance == null) {
                    instance = new QuestionsTableHandler();
                }
            }
        }
        return instance;
    }

    public int Add(Topic topic, Question model) {
        //DBHandler db = new DBHandler();
        String sql = "insert into Question ( QuestionType, QuestionDifficulty, " +
                "QuestionWeight, QuestionExpectedTime, Topic_idTopic) values (?, ?,?,?,?);";
        int last_inserted_question_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                {model.getQuestion_type(), model.getQuestion_diff(), model.getQuestion_weight(), model.getExpected_time(),
                        topic.id});
        for (QuestionContent questionContent : model.getContents()) {
            sql = "insert into QuestionContent (QuestionContent, Question_idQuestion) values (?,?);";
            int last_inserted_question_content_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                    {questionContent.getContent(), last_inserted_question_id + ""});
            for (int i = 0; i < questionContent.getAnswers().size(); i++) {
                sql =
                        "insert into QuestionAnswer (AnswerContent, QuestionContent_idQuestionContent, IsRightAnswer" +
                                ") values (?,?,?);";
                int success = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]{
                        questionContent.getAnswers().get(i).answer_text,
                        last_inserted_question_content_id + "", questionContent.getAnswers().get(i).is_right_answer + ""});
            }
        }
        return last_inserted_question_id;
    }


    public boolean Edit(Question model) {
        //DBHandler db = new DBHandler();
        String sql = "UPDATE Question SET QuestionType = ?, QuestionDifficulty = ?, " +
                "QuestionWeight =?, QuestionExpectedTime =? WHERE idQuestion =?;";

        int last_inserted_question_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                {model.getQuestion_type(), model.getQuestion_diff(), model.getQuestion_weight(), model.getExpected_time(), model.getId()});

        sql = MessageFormat.format("DELETE FROM QuestionContent WHERE Question_idQuestion = {0};", model.getId());
        boolean success1 = DBHandler.getInstance().execute_sql(sql);
        for (QuestionContent questionContent : model.getContents()) {
            sql = "insert into QuestionContent (QuestionContent, Question_idQuestion) values (?,?);";
            int last_inserted_question_content_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                    {questionContent.getContent(), model.getId() + ""});
            questionContent.setId(last_inserted_question_content_id+"");
            sql = MessageFormat.format("DELETE FROM QuestionAnswer WHERE QuestionContent_idQuestionContent = {0};", questionContent.getId());
            boolean success2 = DBHandler.getInstance().execute_sql(sql);
            for (int i = 0; i < questionContent.getAnswers().size(); i++) {
                sql =
                        "insert into QuestionAnswer (AnswerContent, QuestionContent_idQuestionContent, IsRightAnswer" +
                                ") values (?,?,?);";
                int success = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]{
                        questionContent.getAnswers().get(i).answer_text,
                        last_inserted_question_content_id + "", questionContent.getAnswers().get(i).is_right_answer + ""});
            }
        }
        return true;

    }

    public boolean Delete(Question model) {
        String sql = MessageFormat.format("DELETE FROM Question  WHERE idQuestion = {0};", model.getId());
        return DBHandler.getInstance().execute_sql(sql);

    }

    public ObservableList<Question> getQuestionList(Topic topic) {
        questionList = FXCollections.observableArrayList();
        String sql = MessageFormat.format(
                "SELECT * FROM  Question WHERE Topic_idTopic = {0};"
                , topic.id);
        ResultSet rs = DBHandler.getInstance().execute_query(sql);
        parseResultSet(rs);
        return questionList;
    }

    private boolean parseResultSet(ResultSet rs){
        try {
            while (rs.next()) {

                Question model = new Question();
                model.setId(rs.getInt("idQuestion") + "");
                model.setQuestion_diff(rs.getString("QuestionDifficulty"));
                model.setQuestion_type(rs.getString("QuestionType"));
                model.setQuestion_weight(rs.getString("QuestionWeight"));
                model.setExpected_time(rs.getString("QuestionExpectedTime"));

                String sql = MessageFormat.format(
                        "SELECT * FROM  QuestionContent WHERE Question_idQuestion = {0};"
                        , model.getId());
                ResultSet rs2 = DBHandler.getInstance().execute_query(sql);
                List<QuestionContent> temp_content_array;
                temp_content_array = new ArrayList<>();
                while (rs2.next()) {
                    QuestionContent questionContent = new QuestionContent();
                    questionContent.setId(rs2.getInt("idQuestionContent") + "");
                    questionContent.setContent(rs2.getString("QuestionContent") + "");


                    sql = MessageFormat.format(
                            "SELECT * FROM QuestionAnswer where QuestionContent_idQuestionContent = {0};"
                            , questionContent.getId());
                    ResultSet rs3 = DBHandler.getInstance().execute_query(sql);
                    List<Answer> temp_array;
                    temp_array = new ArrayList<>();
                    while (rs3.next()) {
                        Answer answer = new Answer();
                        answer.id = rs3.getInt("idAnswer") + "";
                        answer.answer_text = rs3.getString("AnswerContent");
                        answer.is_right_answer = rs3.getInt("isRightAnswer");
                        temp_array.add(answer);
                    }
                    questionContent.setAnswers(temp_array);
                    temp_content_array.add(questionContent);
                }
                model.setContents(temp_content_array);
                model.setRaw_text(((Jsoup.parse(model.getContents().get(0).getContent()).text())));
                questionList.add(model);
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ObservableList<Question> getQuestionList(Topic topic, String diff) {
        questionList = FXCollections.observableArrayList();
        String sql = MessageFormat.format(
                "SELECT * FROM  Question WHERE Topic_idTopic = {0} AND QuestionDifficulty <= {1};"
                , topic.id, diff);
        ResultSet rs = DBHandler.getInstance().execute_query(sql);
        parseResultSet(rs);
        return questionList;

    }


    public ObservableList<Question> getQuestionList(Chapter chapter) {
        questionList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        //System.out.println("------------------------------------------0");
        String sql = MessageFormat.format(
                "SELECT * FROM ((Question a " +
                        "JOIN Topic b ON a.Topic_idTopic = b.idTopic) " +
                        "JOIN Chapter c ON b.Chapter_idChapter = c.idChapter) " +
                        "where idChapter = {0};"
                , chapter.id);
        ResultSet rs = DBHandler.getInstance().execute_query(sql);
        parseResultSet(rs);
        return questionList;

    }

    public ObservableList<Question> getQuestionList(String ch_id, String diff) {
        questionList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        //System.out.println("------------------------------------------0");
        String sql = MessageFormat.format(
                "SELECT * FROM ((Question a " +
                        "JOIN Topic b ON a.Topic_idTopic = b.idTopic) " +
                        "JOIN Chapter c ON b.Chapter_idChapter = c.idChapter) " +
                        "where idChapter = {0} AND QuestionDifficulty <= \"{1}\";"
                , ch_id, diff);
        ResultSet rs = DBHandler.getInstance().execute_query(sql);
        parseResultSet(rs);
        return questionList;

    }

    public ObservableList<Question> getCachedList() {
        return this.questionList;
    }
}

