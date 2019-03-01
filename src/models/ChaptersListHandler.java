package models;

import controllers.DashboardController;
import controllers.QuestionsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ChaptersListHandler {

    private ObservableList<ChapterModel> chaptersList;
    public boolean Add(ChapterModel model) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into chapter (ChapterName, Course_idCourse) values (\"{0}\",{1}) ;"
                , model.name,DashboardController.current_selected_course_id);
        return db.execute_sql(sql);
    }
    public boolean Edit(ChapterModel model) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE chapter SET ChapterName = \"{0}\" WHERE idChapter = {1} ;"
                , model.name,model.id);
        return db.execute_sql(sql);
    }
    public boolean Delete(ChapterModel model) {
        QuestionsController.questionTableHandler.DeleteAllSelectedChapterQuestions();
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "DELETE  FROM chapter WHERE idChapter = {0} ;"
                , model.id);
        return db.execute_sql(sql);
    }
    public boolean DeleteAllSelectedCourseChapters(){
        for (ChapterModel ch: chaptersList){
            Delete(ch);
        }
        return true;
    }

    public ObservableList<ChapterModel> getChaptersList(){
        chaptersList = FXCollections.observableArrayList();
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM chapter WHERE Course_idCourse ={0};"
                ,  DashboardController.current_selected_course_id);
        ResultSet rs =  db.execute_query(sql);
        try {
            while (rs.next())
            {
                chaptersList.add(new ChapterModel(rs.getInt("idChapter")+"",rs.getString("ChapterName")));
            }
            return chaptersList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            db.closeConnection();
        }

        }
}
