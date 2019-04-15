package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class TopicListHandler {
    private static volatile TopicListHandler instance = null;

    private TopicListHandler() {

    }

    public static TopicListHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (TopicListHandler.class) {
                if (instance == null) {
                    instance = new TopicListHandler();
                }
            }
        }
        return instance;
    }
    
    private ObservableList<Topic> topicList;
    public int Add(Chapter chapter, Topic model) {
        //DBHandler db = new DBHandler();
        String sql = "insert into Topic (Name, Chapter_idChapter) values (?,?) ;";
                String[]params = new String[]{model.name, chapter.id};
        return DBHandler.getInstance().execute_PreparedStatement(sql, params);
    }
    public boolean Edit(Topic model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE Topic SET Name = \"{0}\"  WHERE idTopic = {1} ;"
                , model.name,model.id);
        return DBHandler.getInstance().execute_sql(sql);
    }
    public boolean Delete(Topic model) {
        //QuestionsTableController.questionsTableHandler.DeleteAllSelectedChapterQuestions();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "DELETE  FROM Topic WHERE idTopic = {0} ;"
                , model.id);
        return DBHandler.getInstance().execute_sql(sql);
    }
//    boolean DeleteAllSelectedCourseChapters(){
//        boolean success = false;
//        for (Topic ch: chaptersList){
//            success = Delete(ch);
//        }
//        return success;
//    }

    public ObservableList<Topic> getList(Chapter chapter){
        topicList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM Topic WHERE Chapter_idChapter ={0};"
                ,  chapter.id);
        ResultSet rs =  DBHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                topicList.add(new Topic(rs.getInt("idTopic")+"",rs.getString("Name")));
            }
            return topicList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            //db.disconnect();
        }

    }

    public ObservableList<Topic> getCachedList(){
        return this.topicList;
    }
    
}
