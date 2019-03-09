package models;

import controllers.DashboardController;
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
    
    private ObservableList<TopicModel> topicList;
    public boolean Add(TopicModel model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into topic (Name, Chapter_idChapter) values (\"{0}\",{1}) ;"
                , model.name, DashboardController.current_selected_chapter_id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Edit(TopicModel model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE topic SET Name = \"{0}\"  WHERE idTopic = {1} ;"
                , model.name,model.id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Delete(TopicModel model) {
        QuestionsTableController.questionsTableHandler.DeleteAllSelectedChapterQuestions();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "DELETE  FROM topic WHERE idTopic = {0} ;"
                , model.id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
//    boolean DeleteAllSelectedCourseChapters(){
//        boolean success = false;
//        for (TopicModel ch: chaptersList){
//            success = Delete(ch);
//        }
//        return success;
//    }

    public ObservableList<TopicModel> getTopicsList(){
        topicList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM topic WHERE Chapter_idChapter ={0};"
                ,  DashboardController.current_selected_chapter_id);
        ResultSet rs =  DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                topicList.add(new TopicModel(rs.getInt("idTopic")+"",rs.getString("Name")));
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

    public ObservableList<TopicModel> getCachedList(){
        return this.topicList;
    }
    
}
