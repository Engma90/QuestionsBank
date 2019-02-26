package models;

import controllers.DashboardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ChaptersListHandler {

    private ObservableList<ChapterModel> chaptersList;
    public boolean Add(String name, String course_id) {
        DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "insert into chapter (ChapterName, Course_idCourse) values (\"{0}\",{1}) ;"
                , name,course_id);
        return db.execute_sql(sql);
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
