package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// Todo: remove JSoup from models (Only in views)
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
        String sql = "insert into Question ( QuestionType, QuestionDifficulty, " +
                "QuestionWeight, QuestionExpectedTime, Topic_idTopic) values (?, ?,?,?,?);";
        int last_inserted_question_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                {model.getQuestion_type(), model.getQuestion_diff(), model.getQuestion_weight(), model.getExpected_time(),
                        topic.id});
        for (int i = 0; i < model.getAnswers().size(); i++) {
//            String id_before =  model.getAnswers().get(i).id;
            sql =
                    "insert into QuestionAnswer (AnswerContent, Question_idQuestion" +
                            ") values (?,?);";
            int last_inserted_answer_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]{
                    model.getAnswers().get(i).answer_text,
                    last_inserted_question_id + ""});
            model.getAnswers().get(i).id = last_inserted_answer_id +"";
//            for (QuestionContent questionContent : model.getContents()) {
//                for (Answer rightAnswer : questionContent.getRightAnswers()) {
//                    if(rightAnswer.id.equals(id_before)){
//                        rightAnswer.id = model.getAnswers().get(i).id;
//                    }
//                }
//            }
        }
        for (QuestionContent questionContent : model.getContents()) {
            sql = "insert into QuestionContent (QuestionContent, Question_idQuestion) values (?,?);";
            int last_inserted_question_content_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                    {questionContent.getContent(), last_inserted_question_id + ""});
            questionContent.setId(last_inserted_question_content_id+"");
            for (int i = 0; i < questionContent.getRightAnswers().size(); i++) {
                sql = "insert into ContentRightAnswer (QuestionContent_idQuestionContent, QuestionAnswer_idAnswer) values (?,?);";
                int last_inserted_Right_answer_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                        {last_inserted_question_content_id+"", questionContent.getRightAnswers().get(i).id + ""});
            }

        }
        return last_inserted_question_id;
    }


    public boolean Edit(Question model) {
        //DBHandler db = new DBHandler();
        String sql = "UPDATE Question SET QuestionType = ?, QuestionDifficulty = ?, " +
                "QuestionWeight =?, QuestionExpectedTime =? WHERE idQuestion =?;";

        DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                {model.getQuestion_type(), model.getQuestion_diff(), model.getQuestion_weight(), model.getExpected_time(), model.getId()});

        sql = MessageFormat.format("DELETE FROM QuestionAnswer WHERE Question_idQuestion = {0};", model.getId());
        DBHandler.getInstance().execute_sql(sql);

        for (int i = 0; i < model.getAnswers().size(); i++) {
//            String id_before =  model.getAnswers().get(i).id;
            sql =
                    "insert into QuestionAnswer (AnswerContent, Question_idQuestion" +
                            ") values (?,?);";
            int last_inserted_answer_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]{
                    model.getAnswers().get(i).answer_text,
                    model.getId() + ""});
            model.getAnswers().get(i).id = last_inserted_answer_id +"";
//            for (QuestionContent questionContent : model.getContents()) {
//                for (Answer rightAnswer : questionContent.getRightAnswers()) {
//                    if(rightAnswer.id.equals(id_before)){
//                        rightAnswer.id = model.getAnswers().get(i).id;
//                    }
//                }
//            }
        }

        sql = MessageFormat.format("DELETE FROM QuestionContent WHERE Question_idQuestion = {0};", model.getId());
        boolean success1 = DBHandler.getInstance().execute_sql(sql);
        for (QuestionContent questionContent : model.getContents()) {
            sql = "insert into QuestionContent (QuestionContent, Question_idQuestion) values (?,?);";
            int last_inserted_question_content_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                    {questionContent.getContent(), model.getId() + ""});
            questionContent.setId(last_inserted_question_content_id+"");
            for (int i = 0; i < questionContent.getRightAnswers().size(); i++) {
                sql = "insert into ContentRightAnswer (QuestionContent_idQuestionContent, QuestionAnswer_idAnswer) values (?,?);";
                int last_inserted_Right_answer_id = DBHandler.getInstance().execute_PreparedStatement(sql, new String[]
                        {last_inserted_question_content_id+"", questionContent.getRightAnswers().get(i).id + ""});
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
        runListUpdater(sql);
        return questionList;
    }

    public ObservableList<Question> getQuestionList(Topic topic, String diff) {
        questionList = FXCollections.observableArrayList();
        String sql = MessageFormat.format(
                "SELECT * FROM  Question WHERE Topic_idTopic = {0} AND QuestionDifficulty <= {1};"
                , topic.id, diff);
        runListUpdater(sql);
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
        runListUpdater(sql);
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
        runListUpdater(sql);
        return questionList;

    }



    private void runListUpdater(String SelectionSQL){
        try {

            List<Question> temp = parseQuestionList(SelectionSQL);
            for (Question question:temp){
                question.setAnswers(parseQuestionAnswerList(question));
                question.setContents(parseQuestionContentList(question));
                for (QuestionContent questionContent:question.getContents()){
                    questionContent.setRightAnswers(parseQuestionContentRightAnswerList(question, questionContent));
                }
                question.setRaw_text(((Jsoup.parse(question.getContents().get(0).getContent()).text())));
            }
            questionList.addAll(temp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Question> parseQuestionList(String SelectionSQL) throws SQLException {
        List<Question> temp = FXCollections.observableArrayList();

        ResultSet rs = DBHandler.getInstance().execute_query(SelectionSQL);
        while (rs.next()) {

            Question model = new Question();
            model.setId(rs.getInt("idQuestion") + "");
            model.setQuestion_diff(rs.getString("QuestionDifficulty"));
            model.setQuestion_type(rs.getString("QuestionType"));
            model.setQuestion_weight(rs.getString("QuestionWeight"));
            model.setExpected_time(rs.getString("QuestionExpectedTime"));
            temp.add(model);
        }
        return temp;
    }

    private List<Answer> parseQuestionAnswerList(Question question) throws SQLException {
        List<Answer> temp = FXCollections.observableArrayList();
        String sql = MessageFormat.format(
                "SELECT * FROM QuestionAnswer where Question_idQuestion = {0};"
                , question.getId());
        ResultSet rs3 = DBHandler.getInstance().execute_query(sql);
        while (rs3.next()) {
            Answer answer = new Answer();
            answer.id = rs3.getInt("idAnswer") + "";
            answer.answer_text = rs3.getString("AnswerContent");

            temp.add(answer);
        }
        return temp;
    }

    private List<QuestionContent> parseQuestionContentList(Question question) throws SQLException {
        List<QuestionContent> temp = FXCollections.observableArrayList();
        String sql = MessageFormat.format(
                "SELECT * FROM  QuestionContent WHERE Question_idQuestion = {0};"
                , question.getId());
        ResultSet rs2 = DBHandler.getInstance().execute_query(sql);
        while (rs2.next()) {
            QuestionContent questionContent = new QuestionContent();
            questionContent.setId(rs2.getInt("idQuestionContent") + "");
            questionContent.setContent(rs2.getString("QuestionContent") + "");
            temp.add(questionContent);
        }
        return temp;
    }

    private List<Answer> parseQuestionContentRightAnswerList(Question question, QuestionContent questionContent)
            throws SQLException {
        List<Answer> temp = FXCollections.observableArrayList();
        String sql = MessageFormat.format(
                "SELECT * FROM  ContentRightAnswer WHERE QuestionContent_idQuestionContent = {0};"
                , questionContent.getId());
        ResultSet rs4 = DBHandler.getInstance().execute_query(sql);
        while (rs4.next()) {
            for (Answer answer1:question.getAnswers()){
                if(answer1.id.equals(rs4.getInt("QuestionAnswer_idAnswer") + "")){
                    temp.add(answer1);
                }
            }
        }
        return temp;
    }

//
//    private boolean parseResultSet(ResultSet rs){
//        try {
//            while (rs.next()) {
//
//                Question model = new Question();
//                model.setId(rs.getInt("idQuestion") + "");
//                model.setQuestion_diff(rs.getString("QuestionDifficulty"));
//                model.setQuestion_type(rs.getString("QuestionType"));
//                model.setQuestion_weight(rs.getString("QuestionWeight"));
//                model.setExpected_time(rs.getString("QuestionExpectedTime"));
//
//                String sql = MessageFormat.format(
//                        "SELECT * FROM QuestionAnswer where Question_idQuestion = {0};"
//                        , model.getId());
//                ResultSet rs3 = DBHandler.getInstance().execute_query(sql);
//                List<Answer> temp_array;
//                temp_array = new ArrayList<>();
//                while (rs3.next()) {
//                    Answer answer = new Answer();
//                    answer.id = rs3.getInt("idAnswer") + "";
//                    answer.answer_text = rs3.getString("AnswerContent");
//                    temp_array.add(answer);
//                }
//                model.setAnswers(temp_array);
//
//                sql = MessageFormat.format(
//                        "SELECT * FROM  QuestionContent WHERE Question_idQuestion = {0};"
//                        , model.getId());
//                ResultSet rs2 = DBHandler.getInstance().execute_query(sql);
//                List<QuestionContent> temp_content_array;
//                temp_content_array = new ArrayList<>();
//                while (rs2.next()) {
//                    QuestionContent questionContent = new QuestionContent();
//                    questionContent.setId(rs2.getInt("idQuestionContent") + "");
//                    questionContent.setContent(rs2.getString("QuestionContent") + "");
//                    temp_content_array.add(questionContent);
//
//
//                    sql = MessageFormat.format(
//                            "SELECT * FROM  ContentRightAnswer WHERE QuestionContent_idQuestionContent = {0};"
//                            , questionContent.getId());
//                    ResultSet rs4 = DBHandler.getInstance().execute_query(sql);
//                    List<Answer> temp_Right_answers_array;
//                    temp_Right_answers_array = new ArrayList<>();
//                    while (rs4.next()) {
//                        //Answer answer = new Answer();
//                        //answer.id = rs4.getInt("QuestionAnswer_idAnswer") + "";
//                        for (Answer answer1:model.getAnswers()){
//                            if(answer1.id.equals(rs4.getInt("QuestionAnswer_idAnswer") + "")){
//                                temp_Right_answers_array.add(answer1);
//                            }
//                        }
//                        //temp_Right_answers_array.add(answer);
//                    }
//                    questionContent.setRightAnswers(temp_Right_answers_array);
//                }
//                model.setContents(temp_content_array);
//                model.setRaw_text(((Jsoup.parse(model.getContents().get(0).getContent()).text())));
//                questionList.add(model);
//            }
//
//
//
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }



    public ObservableList<Question> getCachedList() {
        return this.questionList;
    }
}

