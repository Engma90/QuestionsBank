package models;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ChaptersListHandler {
    private ObservableList<Chapter> chaptersList;


    private static volatile ChaptersListHandler instance = null;

    private ChaptersListHandler() {

    }

    public static ChaptersListHandler getInstance() {
        if (instance == null) {
            // To provide thread-safe implementation.
            synchronized (ChaptersListHandler.class) {
                if (instance == null) {
                    instance = new ChaptersListHandler();
                }
            }
        }
        return instance;
    }
    public int Add(Course course, Chapter model) {
        //DBHandler db = new DBHandler();
        String sql = "insert into Chapter (ChapterName, Course_idCourse, ChapterNumber) values (?,?,?) ;";
               String[] params = new String[]{ model.name, course.id,model.number};
        return DBHandler.getInstance().execute_PreparedStatement(sql, params);
    }
    public boolean Edit(Chapter model) {
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "UPDATE Chapter SET ChapterName = \"{0}\", ChapterNumber = {1} WHERE idChapter = {2} ;"
                , model.name,model.number,model.id);
        return DBHandler.getInstance().execute_sql(sql);
    }
    public boolean Delete(Chapter model) {
        //QuestionsTableController.questionsTableHandler.DeleteAllSelectedChapterQuestions();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "DELETE  FROM Chapter WHERE idChapter = {0} ;"
                , model.id);
        return DBHandler.getInstance().execute_sql(sql);
    }
//    boolean DeleteAllSelectedCourseChapters(){
//        boolean success = false;
//        for (Chapter ch: chaptersList){
//            success = Delete(ch);
//        }
//        return success;
//    }

    public ObservableList<Chapter> getList(Course course){
        chaptersList = FXCollections.observableArrayList();
        //DBHandler db = new DBHandler();
        String sql = MessageFormat.format(
                "SELECT * FROM Chapter WHERE Course_idCourse ={0};"
                ,  course.id);
        ResultSet rs =  DBHandler.getInstance().execute_query(sql);
        try {
            while (rs.next())
            {
                chaptersList.add(new Chapter(rs.getInt("idChapter")+"",rs.getString("ChapterName"), rs.getInt("ChapterNumber")+""));
            }
            return chaptersList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            //db.disconnect();
        }

        }

    public ObservableList<Chapter> getCachedList(){
        return this.chaptersList;
    }
}
