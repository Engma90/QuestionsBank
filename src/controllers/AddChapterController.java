package controllers;

import javafx.scene.control.TextField;
import models.ChaptersListHandler;
import models.CoursesListHandler;


public class AddChapterController {

    public TextField chapter_name;
    public void onAddChapterClicked(){
        ChaptersListHandler chaptersListHandler =new ChaptersListHandler();
        boolean success = chaptersListHandler.Add(chapter_name.getText(),DashboardController.current_selected_course_id);
    }
}
