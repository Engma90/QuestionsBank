package models;

import controllers.QuestionsTableController;
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
    public boolean Add(Chapter chapter, Topic model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into topic (Name, Chapter_idChapter) values (\"{0}\",{1}) ;"
                , model.name, chapter.id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Edit(Topic model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE topic SET Name = \"{0}\"  WHERE idTopic = {1} ;"
                , model.name,model.id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Delete(Topic model) {
        QuestionsTableController.questionsTableHandler.DeleteAllSelectedChapterQuestions();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "DELETE  FROM topic WHERE idTopic = {0} ;"
                , model.id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
//    boolean DeleteAllSelectedCourseChapters(){
//        boolean success = false;
//        for (Topic ch: chaptersList){
//            success = Delete(ch);
//        }
//        return success;
//    }

    public ObservableList<Topic> getTopicsList(Chapter chapter){
        topicList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM topic WHERE Chapter_idChapter ={0};"
                ,  chapter.id);
        ResultSet rs =  DBSingletonHandler.getInstance().execute_query(sql);
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
            //db.closeConnection();
        }

    }

    public ObservableList<Topic> getCachedList(){
        return this.topicList;
    }
    
}
