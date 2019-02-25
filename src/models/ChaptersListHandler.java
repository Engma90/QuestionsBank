package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        chaptersList.add(new ChapterModel("1","Ch1"));
        chaptersList.add(new ChapterModel("2","Ch2"));
        return chaptersList;
    }
}
