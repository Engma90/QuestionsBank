import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import models.Chapter;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChapterTest extends TestBaseSkipLogin {

    private final String DR_ID = "1";
    private final String CONTAINER = "#chapters";
    private final String TABLE = "#chapters_list_view";
    private final String BUTTON_ADD = "#btn_add";
    private final String BUTTON_EDIT = "#btn_edit";
    private final String BUTTON_DELETE = "#btn_delete";

    private final String COURSE_CODE = "1";
    private final String COURSE_NAME = "Chapter1";
    private final String COURSE_CODE_EDITED = "2";
    private final String COURSE_NAME_EDITED = "Chapter2";
    private final String COURSE_CODE_FIELD = "#chapter_number";
    private final String COURSE_NAME_FIELD = "#chapter_name";
    private final String COURSE_WINDOW_ADD_BUTTON = "#add_chapter";
    private final String COURSE_WINDOW_EDIT_BUTTON = "#edit_chapter";

    //private CourseTest courseTest;


    private ListView<Chapter> listView;

//    @Override
//    public void start(Stage stage) throws Exception {
//        LoginHandler.getInstance().login("a","a");
//        new WindowLoader().load(null, "/views/Dashboard.fxml", null, null, false, false, null);
//        WaitForAsyncUtils.waitForFxEvents(1);
//    }
//
//    @After
//    public void atferEachTest() throws TimeoutException {
//        //
//        FxToolkit.cleanupStages();
//        FxToolkit.hideStage();
//        release(new KeyCode[] {});
//        release(new MouseButton[] {});
//    }

    @Test
    public void Test1AddChapter(){
        new CourseTest().Test1AddCourse();
        clickOn(CONTAINER + " " + BUTTON_ADD);
        clickOn(COURSE_CODE_FIELD).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.NUMPAD0);

        clickOn(COURSE_CODE_FIELD).write(COURSE_CODE);
        clickOn(COURSE_NAME_FIELD).write(COURSE_NAME);
        clickOn(COURSE_WINDOW_ADD_BUTTON);
        listView = lookup(CONTAINER).lookup(TABLE).query();

        Chapter last = listView.getItems().get(listView.getItems().size() - 1);
        verifyThat(last.name, hasText(COURSE_NAME));
    }

    @Test
    public void Test2EditChapter(){
        new CourseTest().selectLast();
        listView = lookup(CONTAINER).lookup(TABLE).query();
        WaitForAsyncUtils.waitForFxEvents();
        listView.getSelectionModel().selectLast();
        clickOn(CONTAINER + " " + BUTTON_EDIT);

        clickOn(COURSE_CODE_FIELD).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.NUMPAD0);
        clickOn(COURSE_NAME_FIELD).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.DELETE);

        clickOn(COURSE_CODE_FIELD).write(COURSE_CODE_EDITED);
        clickOn(COURSE_NAME_FIELD).write(COURSE_NAME_EDITED);
        clickOn(COURSE_WINDOW_EDIT_BUTTON);

        listView = lookup(CONTAINER).lookup(TABLE).query();
        String last = listView.getItems().get(listView.getItems().size() - 1).name;
        verifyThat(last, hasText(COURSE_NAME_EDITED));
    }


    @Test
    public void Test3DeleteChapter(){
        new CourseTest().selectLast();
        WaitForAsyncUtils.waitForFxEvents(1);
        listView = lookup(CONTAINER).lookup(TABLE).query();
        listView.getSelectionModel().selectLast();
        Chapter last = listView.getItems().get(listView.getItems().size() - 1);
        int size = listView.getItems().size();
        clickOn(CONTAINER + " " + BUTTON_DELETE);
        clickOn("Yes");
        WaitForAsyncUtils.waitForFxEvents(1);
        FxAssert.verifyThat(listView, ListViewMatchers.hasItems(size - 1));

        new CourseTest().Test3DeleteCourse();
    }

    public void selectLast(){
        new CourseTest().selectLast();
        listView = lookup(CONTAINER).lookup(TABLE).query();
        WaitForAsyncUtils.waitForFxEvents();
        listView.getSelectionModel().selectLast();
    }

}
