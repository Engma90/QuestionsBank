package models;

import controllers.DashboardController;
import controllers.QuestionsTableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ChaptersListHandler {
    private ObservableList<ChapterModel> chaptersList;
    public boolean Add(ChapterModel model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into chapter (ChapterName, Course_idCourse, ChapterNumber) values (\"{0}\",{1},{2}) ;"
                , model.name,DashboardController.current_selected_course_id,model.number);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Edit(ChapterModel model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE chapter SET ChapterName = \"{0}\", ChapterNumber = {1} WHERE idChapter = {2} ;"
                , model.name,model.number,model.id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
    public boolean Delete(ChapterModel model) {
        QuestionsTableController.questionsTableHandler.DeleteAllSelectedChapterQuestions();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "DELETE  FROM chapter WHERE idChapter = {0} ;"
                , model.id);
        return DBSingletonHandler.getInstance().execute_sql(sql);
    }
//    boolean DeleteAllSelectedCourseChapters(){
//        boolean success = false;
//        for (ChapterModel ch: chaptersList){
//            success = Delete(ch);
//        }
//        return success;
//    }

    public ObservableList<ChapterModel> getChaptersList(){
        chaptersList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM chapter WHERE Course_idCourse ={0};"
                ,  DashboardController.current_selected_course_id);
        ResultSet rs =  DBSingletonHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                chaptersList.add(new ChapterModel(rs.getInt("idChapter")+"",rs.getString("ChapterName"), rs.getInt("ChapterNumber")+""));
            }
            return chaptersList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            //db.closeConnection();
        }

        }

    public ObservableList<ChapterModel> getCachedList(){
        return this.chaptersList;
    }
}
